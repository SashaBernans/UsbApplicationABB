package app.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileSystemView;

/**
 *@author Sasha Bernans
 *This class extends swingWorker so that multithreading is possible.
 *It contains the methods necessary for copying the image to a destination.
 *Copying an image at the moment is dependant on the directory and file structure of the default path : L:\\Installs\\SOFT\\SOFTINV\\AAYYYYYY-ZZ
 *if the structure changes this code may need to be modified.
 */
public class ImageCopier extends SwingWorker{
	private static final int SPANNED_ARCHIVE_BIT_SIGNATURE = 0x504B0708;
	private static final int EMPTY_ARCHIVE_BIT_SIGNATURE = 0x504B0506;
	private static final int ARCHIVE_BIT_SIGNATURE = 0x504B0304;
	private static final String STANDARD_OUTPUT_MESSAGE = "Standard Output:";
	private static final String DONE_MESSAGE = "Done";
	private static final String STANDARD_ERROR_MESSAGE = "Standard Error:";
	private static final String DUPLICATE_SOFTWARE_MESSAGE = "Duplicate software directories have been found, copy all? \n";
	private Image image;
	private String destination;
	private String defaultPath;
	private String TIBPath;
	private ArrayList<String> directoriesToCopy;
	
	/**
	 * @param image : contains the files paths and the information needed to create the image on the USB drive
	 * @param destination : the destination directory to copy image to (should be USB drive)
	 */
	public ImageCopier(Image image, String destination) {
		if(!this.netDriveIsMapped()) {
			this.mapNetDrive();
		}
		this.directoriesToCopy = new ArrayList<String>();
		this.setImage(image);
		this.setDestination(destination);
		this.defaultPath = image.getDefaultPath();
	}

	
	/**
	 * Formats and then Copies the directories and files necessary for creating the image on the USB drive.
	 */
	public void copyImageToUsb()  {
		//Formats USB drive but it is commented because this version is for already formatted drives.
		this.formatUsbDrive();
		
		//Finds the directories to copy.
		try {
			Files.walkFileTree(Paths.get(this.defaultPath),new SoftwareFolderVisitor(this));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		//Checks if there are two directories that start with the same part number
		this.checkForDuplicates(directoriesToCopy);
		
		System.out.println(directoriesToCopy);
		
		//Finds the .tib file to copy.
		try {
			Files.walkFileTree(Paths.get(this.defaultPath),new TIBFileVisitor(this));
		} catch (IOException e) {
			e.printStackTrace();
		}	
		System.out.println(TIBPath);
		
		//Writes the information that the user input in the mainView to a .txt file
		this.writeCustumerInformationToFile();
		
		//Copies the .tib file to USB root.
		this.copyFileToDestination(this.getDestination(), TIBPath);
		
		//Copies the directories, sub-directories and files needed to the USB root.
		directoriesToCopy.forEach(dir ->{
			this.copyDirectoryAndContentsToDestination(dir, getDestination());
		});
	}

	
	/**
	 * This finds duplicate software directories and asks the user if they want to copy all of them or only the first one.
	 * Then if the user confirm NO_OPTION the second one is removed from directories to copy.
	 * @param directoriesToCopy : the list of directories to find duplicates in
	 */
	private void checkForDuplicates(ArrayList<String> directoriesToCopy) {
		ArrayList<String> duplicates = new ArrayList<String>();
		for(int i = 0; i<image.getSoftwareFolderNames().size();i++) {
			for(int j = 0; j<directoriesToCopy.size();j++){
				if(Paths.get(directoriesToCopy.get(j)).getFileName().toString().startsWith(image.getSoftwareFolderNames().get(i))) {
					duplicates.add(directoriesToCopy.get(j));
				}
				if(duplicates.size()>1 && j==directoriesToCopy.size()-1) {
					if(!this.askUserToCopyAll(duplicates)) {
						for(int k=duplicates.size()-1;k>0;k--) {
							directoriesToCopy.remove(k);
						}
					}
				}
			}
		}
	}


	/**
	 * This ask the user if they want to copy all duplicate directories or not.
	 * @param duplicates : the list of duplicate directories
	 * @return true if yes, false if no
	 */
	private Boolean askUserToCopyAll(ArrayList<String> duplicates) {
		String lines = "";
		for(int i = 0; i<duplicates.size();i++) {
			lines = lines.concat(i+1+" : "+duplicates.get(i)+"\n");
		}
		String message = DUPLICATE_SOFTWARE_MESSAGE+lines;
		int  userInput = JOptionPane.showConfirmDialog(null,
				message, 
				"Warning",
				JOptionPane.YES_NO_OPTION);
		if(userInput==JOptionPane.YES_OPTION) {
			return true;
		}
		else {
			return false;
		}
	}


	/**
	 * Creates and writes the information that user input in the mainView to a .txt file.
	 */
	private void writeCustumerInformationToFile() {
		//Creates list containing all lines to be written to text file.
	    List<String> lines = Arrays.asList("Sales Order: "+this.image.getSalesOrder(),
	    		"Work Order: "+this.image.getWorkOrder(),
	    		"Customer's name: "+image.getCustomerName(),
	    		"Description: "+image.getDescription());
	    
	    //Creates .txt file
	    Path file = Paths.get(destination+this.image.getSalesOrder()+"_"+this.image.getWorkOrder()+".txt");
	    
	    //Writes the lines to the file created
	    try {
			Files.write(file, lines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * This method creates a USB formatter Object and calls the method to format the drive.
	 */
	private void formatUsbDrive() {
		UsbFormatter formatter = new UsbFormatter(this.destination, this.image.getWorkOrder());
		formatter.formatToNTFS();
	}

	/**
	 * This copies a file to a directory, throws filesAlreadyExist
	 * if the file already exists in the destination directory.
	 * @param destinationPath : path to destination directory
	 * @param filePath : path of file to be copied
	 */
	private void copyFileToDestination(String destinationPath, String filePath) {
		Path source = Paths.get(filePath);
		Path destination = Paths.get(destinationPath+source.getFileName());
		try {
			Files.copy(source, destination);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns a Stream of the paths contained in the directory at 1 level deep. 
	 * If the directory isn't readable or the path is for a file, a stream containing only the directory path is returned.
	 * @param p : path to directory
	 * @return a stream of of the directory content
	 */
	private Stream<Path> walkReadable(Path p) {
		if(Files.isReadable(p)) {
			if(Files.isDirectory(p)) {
				try {
					return Files.list(p);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return Stream.of(p);
		}
		return Stream.of(p);
    }

	/**
	 * This copies a directory, its sub-directories and its files to a destination 
	 * directory while preserving the original structure, but it does not copy the archive 
	 * file that is named the same as the directory.
	 * @param sourceDirectory : the directory to copy
	 * @param destinationDirectory : the directory to copy to
	 */
	private void copyDirectoryAndContentsToDestination(String sourceDirectory,String destinationDirectory){
        Path fromPath = Paths.get(sourceDirectory);
        Path toPath = Paths.get(destinationDirectory);
        
        try {
			Files.walk(fromPath)
				//For each file in sourceDirectory copy it to destination
			     .forEach(source -> copySourceToDest(source,toPath,fromPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * this copies a file to a destination determined by its own path and the toPath specified.
     * @param source : path of the file being copied
     * @param toPath : path of the destination the file is being copied to
     * @param fromPath : the root directory of the source's directory structure
     */
    private static void copySourceToDest(Path source,Path toPath,Path fromPath) {
    	Boolean canBeCopied = true;
    	String subfolder = "";
    	
    	if(!source.equals(fromPath)) {
    		subfolder = source.toString().substring(fromPath.toString().length()+1);
        }
    	
    	//this gets destination for the file being copied
        Path destination = Paths.get(toPath.toString()+"\\"+fromPath.getFileName().toString()
        		, source.toString().substring(fromPath.toString().length()));
        //This extracts the part number of the source directory
        String partNumber = fromPath.getFileName().toString().substring(0, 11);
        //This checks if the file can be copied 
    	if(subfolder.startsWith(partNumber)) {
        	canBeCopied = false;
    	}
        try {
        	if(canBeCopied) {
        		Files.copy(source, destination);
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * NOTE: this method cannot be used at the moment because it requires some permissions the user
     * won't have on the file.
     * 
     * 
     * this checks if a file is a zip by checking its bits signature
     * @param f : the file to check
     * @return true if the file is a zip archive false if not.
     */
    private static boolean isArchive(File f) {
        int fileSignature = 0;
        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
            fileSignature = raf.readInt();
        } catch (IOException e) {
           e.printStackTrace();
        }
        return fileSignature == ARCHIVE_BIT_SIGNATURE || fileSignature == EMPTY_ARCHIVE_BIT_SIGNATURE || fileSignature == SPANNED_ARCHIVE_BIT_SIGNATURE;
    }
    
    
    /**
     * 
     * This checks if the L: drive is mapped to apps (\\\\caabbqubf1001)
     * @return true if the the L: drive is mapped false if not
     */
    private boolean netDriveIsMapped() {
    	File[] paths;
		// Gets all drives
		paths = File.listRoots();
		
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// for each pathname in pathname array
		for(File path:paths)
		{
			if(fsv.getSystemDisplayName(path).equals("apps (\\\\caabbqubf1001) (L:)")) {
				return true;
			}
		}
		return false;
    }
    
    /**
     * this maps the network drive L: using a powershell.exe command
     */
    private void mapNetDrive() {
		try {
			//execute mapping in a powershell.exe process.
			Process process = Runtime.getRuntime().exec(new String[]{"powershell.exe", "/c","net use L: \\\\caabbqubf1001\\apps"});
		
			// printing the results
			  process.getOutputStream().close();
			  String line;
			  System.out.println(STANDARD_OUTPUT_MESSAGE);
			  BufferedReader stdout = new BufferedReader(new InputStreamReader(
			    process.getInputStream()));
			  while ((line = stdout.readLine()) != null) {
			   System.out.println(line);
			  }
			  
			//Print out errors
			  stdout.close();
			  System.out.println(STANDARD_ERROR_MESSAGE);
			  BufferedReader stderr = new BufferedReader(new InputStreamReader(
			    process.getErrorStream()));
			  while ((line = stderr.readLine()) != null) {
			   System.out.println(line);
			  }
			  stderr.close();
			  System.out.println(DONE_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@Override
	protected Object doInBackground() throws Exception {
		this.copyImageToUsb();
		return null;
	}


	public void setTIBPath(String path) {
		this.TIBPath = path;
	}


	public void addDirectoryToCopy(String dir) {
		this.directoriesToCopy.add(dir);
	}
}
