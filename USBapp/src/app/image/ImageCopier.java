package app.image;

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

public class ImageCopier extends SwingWorker{
	private Image image;
	private String destination;
	private boolean isDone = false;
	
	public ImageCopier(Image image, String destination) {
		this.setImage(image);
		this.setDestination(destination);
	}

	public void copyImageToUsb()  {
		
		this.formatUsbDrive();
		ArrayList<String> directoriesToCopy = new ArrayList<String>();
		getImage().getSoftwareFolderNames().forEach(folder ->{
			directoriesToCopy.addAll(this.findDirectoryStartingWith(folder, ImageConstants.IMAGE_FOLDER));
		});
		System.out.println(directoriesToCopy);
		
		String TIBPath = findTIBFileStartingWith(getImage().getTIBName(), ImageConstants.IMAGE_FOLDER);
		
		System.out.println(TIBPath);
		
		this.writeCustumerInformationToFile();
		/*
		copyFileToUsbRoot(getDestination(), TIBPath);
		
		directoriesToCopy.forEach(dir ->{
			this.copyDirectoryAndContentsToDestination(dir, getDestination());
		});
		*/
		//Notify controller that this thread is completed
		this.isDone = true;
	}

	private void writeCustumerInformationToFile() {
	    List<String> lines = Arrays.asList("Sales Order: "+this.image.getSalesOrder(),
	    		"Work Order: "+this.image.getWorkOrder(),
	    		"Customer's name: "+image.getCustomerName(),
	    		"Description: "+image.getDescription());
	    Path file = Paths.get(destination+this.image.getSalesOrder()+"_"+this.image.getWorkOrder()+".txt");
	    try {
			Files.write(file, lines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void formatUsbDrive() {
		UsbFormatter formatter = new UsbFormatter(this.destination, this.image.getWorkOrder());
		formatter.formatToNTFS();
	}

	private void copyFileToUsbRoot(String destination, String TIBPath) {
		try {
			TIBPath =this.formatPathForPowerShell(TIBPath);
			// Executing the command
			Process powerShellProcess = Runtime.getRuntime().exec(new String[]{"powershell.exe", "/c","Copy-Item '"+TIBPath+"' -Destination '"+destination+"'"});
			  
			// printing the results
			powerShellProcess.getOutputStream().close();
			String line;
			System.out.println("Standard Output:");
			    BufferedReader stdout = new BufferedReader(new InputStreamReader(
			      powerShellProcess.getInputStream()));
			    while ((line = stdout.readLine()) != null) {
			     System.out.println(line);
			    }
			    stdout.close();
			  
			//Print out errors
			System.out.println("Standard Error:");
			BufferedReader stderr = new BufferedReader(new InputStreamReader(
			  powerShellProcess.getErrorStream()));
			while ((line = stderr.readLine()) != null) {
			 System.out.println(line);
			}
			stderr.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String formatPathForPowerShell(String path) {
		return path.replace("&", "\"&\"");
	}

	private String findTIBFileStartingWith(String startingPartOfFile, String imageFolder) {
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> folderPath = findDirectoryStartingWith(startingPartOfFile,ImageConstants.IMAGE_FOLDER);
		String pathOfDirectory; 
		if(folderPath.isEmpty()) {
			pathOfDirectory = ImageConstants.IMAGE_FOLDER;
		}
		else{
			pathOfDirectory = folderPath.get(0);
		}
		
		walkReadable(Paths.get(pathOfDirectory))
			.forEach(file ->{
				if(file.getFileName().toString().startsWith(startingPartOfFile)) {
					files.add(file.toString());
				}
			});
		
		return files.get(0);
	}

	private ArrayList<String> findDirectoryStartingWith(String startingPartOfDirectory, String sourceDirectoryLocation) {
		ArrayList<String> files = new ArrayList<String>();
			walkReadable(Paths.get(sourceDirectoryLocation))
				.forEach(file -> {
					if(Files.isDirectory(file)) {
						if(file.getFileName().toString().startsWith(startingPartOfDirectory)) {
							files.add(file.toString());
						}
					}
				});
		return files;
	}
	
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

	private void copyDirectoryAndContentsToDestination(String sourceDirectory,String destinationDirectory){
        Path fromPath = Paths.get(sourceDirectory);
        Path toPath = Paths.get(destinationDirectory);

        try {
			Files.walk(fromPath)
			     .forEach(source -> copySourceToDest(fromPath, source,toPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private static void copySourceToDest(Path fromPath, Path source,Path toPath) {
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
