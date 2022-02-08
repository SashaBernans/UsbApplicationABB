package app.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.SwingWorker;

/**
 *@author Sasha Bernans
 *This class extends swingWorker so that multithreading is possible.
 *It contains the methods necessary for copying the image to a destination.
 *Copying an image at the moment is dependant on the directory and file structure of the default path : L:\\Installs\\SOFT\\SOFTINV\\AAYYYYYY-ZZ
 *if the structure changes this code may need to be modified.
 */
public class ImageCopier extends SwingWorker{
	private Image image;
	private String destination;
	
	/**
	 * @param image : contains the files paths and the information needed to create the image on the USB drive
	 * @param destination : the destination directory to copy image to (should be USB drive)
	 */
	public ImageCopier(Image image, String destination) {
		this.setImage(image);
		this.setDestination(destination);
	}

	
	/**
	 * Formats and then Copies the directories and files necessary for creating the image on the USB drive.
	 */
	public void copyImageToUsb()  {
		//Formats USB drive.
		this.formatUsbDrive();
		
		//Finds the directories to copy.
		ArrayList<String> directoriesToCopy = new ArrayList<String>();
		getImage().getSoftwareFolderNames().forEach(folder ->{
			directoriesToCopy.addAll(this.findDirectoriesStartingWith(folder, ImageConstants.IMAGE_FOLDER));
		});
		System.out.println(directoriesToCopy);
		
		//Finds the .tib file to copy.
		String TIBPath = findPathOfFileStartingWith(getImage().getTIBName(), ImageConstants.IMAGE_FOLDER);
		
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
	 * This copies a file to a directory
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
	 * This method searches a directory to find a file starting with a String.
	 * If the file is contained in a sub-directory that sub-directory must start with the same string as well, or
	 * null will be returned. If the file is more than 1 sub-directory deep then null will be returned.
	 * @param startingPartOfFile : a string that represents the starting part of the file name.
	 * @param directoryContainingFile : the directory to be searched
	 * @return the path of the file as a String
	 */
	private String findPathOfFileStartingWith(String startingPartOfFile, String directoryContainingFile) {
		ArrayList<String> files = new ArrayList<String>();
		
		//Find the directory that contains the file
		ArrayList<String> dirPaths = findDirectoriesStartingWith(startingPartOfFile,ImageConstants.IMAGE_FOLDER);
		
		String dirPath;
		
		//if directory not found then use default path
		if(dirPaths.isEmpty()) {
			dirPath = ImageConstants.IMAGE_FOLDER;
		}
		else {
			dirPath = dirPaths.get(0);
		}
		
		//Stream the paths contained in the directory to find the file starting with the startingPartOfFile String.
		walkReadable(Paths.get(dirPath))
			.forEach(file ->{
				if(file.getFileName().toString().startsWith(startingPartOfFile)) {
					files.add(file.toString());
				}
			});
		if(files.isEmpty()) {
			return null;
		}
		
		return files.get(0);
	}

	/**
	 * This method finds a ArrayList of directories that their names start with the desired string.
	 * It does not search sub-directories. If the source directory does not contain any directories with starting
	 * with the specified string then an empty array is returned.
	 * @param startingPartOfDirectory : a string that represents the starting part of the directory name.
	 * @param sourceDirectoryLocation : the directory path containing the directories searched for
	 * @return a ArrayList of the directories starting with the specified string.
	 */
	private ArrayList<String> findDirectoriesStartingWith(String startingPartOfDirectory, String sourceDirectoryLocation) {
		ArrayList<String> files = new ArrayList<String>();
		//Gets stream of sub-directories if sourceDirectoryLocation is readable
			walkReadable(Paths.get(sourceDirectoryLocation))
			//For each sub-directory add it to files if it starts with startingPartOfDirectory
				.forEach(file -> {
					if(Files.isDirectory(file)) {
						if(file.getFileName().toString().startsWith(startingPartOfDirectory)) {
							files.add(file.toString());
						}
					}
				});
		return files;
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
	 * directory while preserving the original structure.
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
     * @param toPath : path of the destination the structure is being copied to
     */
    private static void copySourceToDest(Path source,Path toPath,Path fromPath) {
    	//this gets destination for the file being copied
        Path destination = Paths.get(toPath.toString()+"\\"+fromPath.getFileName().toString()
        		, source.toString().substring(fromPath.toString().length()));
        try {
            Files.copy(source, destination);
            
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
}
