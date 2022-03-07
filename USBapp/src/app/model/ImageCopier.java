package app.model;

import static java.nio.file.FileVisitResult.TERMINATE;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
public class ImageCopier extends SwingWorker<Image,String>{
	private static final String FATAL_ERROR = "FATAL ERROR \n";
	private static final String WRITING_CUSTOMER_INFORMATION_ERROR = "Something happened while writing customer information to file : \n";
	private static final String ISO_NOT_FOUND = "ISO file : "+Constants.ISO+" not found";
	private static final String ISO_FILE_ERROR = "Error while looking for ISO file : \n";
	private static final String MAPPING_DRIVE_ERROR = "An error occured while mapping network drive : \n";
	private static final String SOFTWARE_FOLDER_COPY_ERROR = "An error occured while copying the software folder : \n";
	private static final String TIB_FILE_COPY_ERROR = "An error occured while copying the file : \n";
	private static final String TIB_ERROR = "An error occured while searching for the .tib file : \n";
	private static final String TIB_NOT_FOUND = "The following .tib file was not found : \n";
	private static final String SOFTWARE_NOT_FOUND = "The following software(s) folder(s) was/were not found : \n";
	private static final String SOFTWARE_FOLDERS_ERROR = "an error occured while searching for software folders : \n ";
	private static final String STANDARD_OUTPUT_MESSAGE = "Standard Output:";
	private static final String DONE_MESSAGE = "Done";
	private static final String STANDARD_ERROR_MESSAGE = "Standard Error:";
	private static final String DUPLICATE_SOFTWARE_MESSAGE = "Duplicate software directories have been found, copy all? \n If no is selected only the first directory will be copied.\n";
	private Image image;
	private String destination;
	private String defaultPath;
	private String TIBPath;
	private ArrayList<String> directoriesToCopy;
	private String ISOPath;
	
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
		//Formats USB drive
		this.formatUsbDrive();
		
		//Finds the ISO file to copy.
		try {
			Files.walkFileTree(Paths.get(this.defaultPath),new ISOFileVisitor(this));
		} catch (IOException e) {
			e.printStackTrace();
			alertUser(FATAL_ERROR+ISO_FILE_ERROR+e.toString());
			System.exit(0);
		}
		finally {
			if(ISOPath==null) {
				alertUser(ISO_NOT_FOUND);
			}
		}
		System.out.println("ISO : "+this.ISOPath);
		System.out.println("DESTINATION : "+this.getDestination());
		//Copies the ISO directory contents to the destination
		this.copyDirectoryContentsToDestination(ISOPath, this.getDestination());

		//Finds the software directories to copy.
		if(!this.image.getSoftwareFolderNames().isEmpty()) {
			try {
				Files.walkFileTree(Paths.get(this.defaultPath),new SoftwareFolderVisitor(this));
			} catch (IOException e) {
				e.printStackTrace();
				alertUser(FATAL_ERROR+SOFTWARE_FOLDERS_ERROR+e.toString());
				System.exit(0);
			}
			finally {
				if(directoriesToCopy.size()<image.getSoftwareFolderNames().size()) {
					String softwaresNotFound = findMissingSoftwareDirectories();
					alertUser(SOFTWARE_NOT_FOUND + softwaresNotFound);
				}
			}
		}
		
		//Checks if there are two directories that start with the same part number
		if(directoriesToCopy.size()>image.getSoftwareFolderNames().size()) {
			this.checkForDuplicates(directoriesToCopy);
		}
		
		System.out.println("new directories to copy : " + directoriesToCopy);
		
		//Finds the .tib file to copy.
		try {
			Files.walkFileTree(Paths.get(this.defaultPath),new TIBFileVisitor(this));
		} catch (IOException e) {
			e.printStackTrace();
			alertUser(FATAL_ERROR+TIB_ERROR+e.toString());
			System.exit(0);
		}
		finally {
			if(TIBPath==null) {
				alertUser(TIB_NOT_FOUND + image.getTIBName());
			}
		}
		System.out.println("The tib file found : "+TIBPath);
		
		//Writes the information that the user input in the mainView to a .txt file
		this.writeCustumerInformationToFile();
		
		//Copies the .tib file to USB root.
		this.copyFileToDestination(this.getDestination(), TIBPath);
		
		//Copies the directories, sub-directories and files needed to the USB root.
		if(!directoriesToCopy.isEmpty()) {
			directoriesToCopy.forEach(dir ->{
				this.copyDirectoryAndContentsToDestination(dir, getDestination());
			});
		}
	}


	/**
	 * This finds the missing directories in directories to copy
	 * @return a string containing the part number of each missing directory
	 */
	private String findMissingSoftwareDirectories() {
		String softwaresNotFound = "";
		boolean contains;
		for(int i =0;i<image.getSoftwareFolderNames().size();i++) {
			contains= false;
			for(int j =0;j<directoriesToCopy.size();j++) {
				if(Paths.get(directoriesToCopy.get(j)).getFileName().toString().startsWith(image.getSoftwareFolderNames().get(i))) {
					contains = true;
				}
			}
			if(!contains) {
				softwaresNotFound = softwaresNotFound+image.getSoftwareFolderNames().get(i)+"\n";
			}
		}
		return softwaresNotFound;
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
					//If user answers no then remove the duplicates
					if(!this.askUserToCopyAll(duplicates)) {
						for(int k=duplicates.size()-1;k>0;k--) {
							directoriesToCopy.remove(k);
						}
						duplicates = new ArrayList<String>();
					}
				}
			}
		}
	}

	/**
	 * This ask the user if they want to copy all duplicate directories or only the first.
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
			alertUser(WRITING_CUSTOMER_INFORMATION_ERROR+e.toString());
		}
	}

	
	/**
	 * This method creates a USB formatter Object and calls the method to format the drive.
	 */
	private void formatUsbDrive() {
		UsbFormatter formatter = new UsbFormatter(this.destination, this.image.getWorkOrder());
		formatter.formatToNTFS();
	}
	
	private void copyDirectoryContentsToDestination(String source, String destination) {
		Path sourcePath = Paths.get(source);
		
		File[] contents = sourcePath.toFile().listFiles();
		
		for(int i=0; i<contents.length;i++) {
			if(contents[i].isDirectory()) {
				this.copyDirectoryAndContentsToDestination(contents[i].toString(), destination);
			}
			else{
				this.copyFileToDestination(destination, contents[i].toString());
			}
	   	}
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
			alertUser(FATAL_ERROR+TIB_FILE_COPY_ERROR +e.toString());
			System.exit(0);
		}
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
			     .forEach(source -> {
					try {
						copySourceToDest(source,toPath,fromPath);
					} catch (IOException e) {
						e.printStackTrace();
						alertUser(FATAL_ERROR+SOFTWARE_FOLDER_COPY_ERROR +source.toString()+"\n"+ e.toString());
						System.exit(0);
					}
				});
		} catch (IOException e) {
			e.printStackTrace();
			alertUser(FATAL_ERROR+" An error occured while iterating threw : "+sourceDirectory+"\n error : " + e.toString());
			System.exit(0);
		}
    }

    /**
     * this copies a file to a destination determined by its own path and the toPath specified.
     * @param source : path of the file being copied
     * @param toPath : path of the destination the file is being copied to
     * @param fromPath : the root directory of the source's directory structure
     */
    private static void copySourceToDest(Path source,Path toPath,Path fromPath) throws IOException{
    	String subFolder = "";
    	
    	if(!source.equals(fromPath)) {
    		subFolder = source.toString().substring(fromPath.toString().length()+1);
        }
    	
    	//this gets destination for the file being copied
        Path destination = Paths.get(toPath.toString()+"\\"+fromPath.getFileName().toString()
        		, source.toString().substring(fromPath.toString().length()));
        //This extracts the part number of the source directory
        String partNumber = null;
        if(fromPath.getFileName().toString().length()>=11) {
        	partNumber = fromPath.getFileName().toString().substring(0, 11);
        }
        //This checks if the file can be copied 
        if(partNumber!=null) {
        	if(!subFolder.startsWith(partNumber) && !subFolder.endsWith(".zip")) {
        		Files.copy(source, destination);
        	}
        }
        else {
        	Files.copy(source, destination);
        }
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
			alertUser(MAPPING_DRIVE_ERROR+e.toString());
		}
    }
    private void alertUser(String error) {
		if(error!=null) {
		JOptionPane.showMessageDialog(null,
			    error,
			    "ERROR",
			    JOptionPane.ERROR_MESSAGE);
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
	protected Image doInBackground() throws Exception {
		this.copyImageToUsb();
		return null;
	}


	public void setTIBPath(String path) {
		this.TIBPath = path;
	}


	public void addDirectoryToCopy(String dir) {
		this.directoriesToCopy.add(dir);
	}


	public void setISOPath(String path) {
		this.ISOPath = path;
	}
}
