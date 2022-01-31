package app.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ImageCopier {
	public ImageCopier() {
		
	}

	public void copyImageToUsb(Image image, String destination)  {
		ArrayList<String> directoriesToCopy = new ArrayList<String>();
		image.getSoftwareFolderNames().forEach(folder ->{
			directoriesToCopy.addAll(this.findDirectoryStartingWith(folder, ImageConstants.IMAGE_FOLDER));
		});
		System.out.println(directoriesToCopy);
		
		String TIBPath = findTIBFileStartingWith(image.getTIBName(), ImageConstants.IMAGE_FOLDER);
		
		System.out.println(TIBPath);
		
		directoriesToCopy.forEach(dir ->{
			try {
				this.copyDirectory(dir, destination);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		renameDrive(image.getWorkOrder(), destination);
	}

	private String findTIBFileStartingWith(String startingPartOfFile, String imageFolder) {
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> folderPath = findDirectoryStartingWith(startingPartOfFile,ImageConstants.IMAGE_FOLDER);
		walkReadable(Paths.get(folderPath.get(0)))
			.forEach(file ->{
				if(file.getFileName().toString().startsWith(startingPartOfFile)) {
					files.add(file.toString());
				}
			});
		
		return files.get(0);
	}

	private void renameDrive(String workOrder, String drive) {
		try {
			Runtime.getRuntime().exec("label "+drive.replace("\\","")+" "+workOrder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation) 
			  throws IOException {
			    Files.walk(Paths.get(sourceDirectoryLocation))
			      .forEach(source -> {
			          Path destination = Paths.get(destinationDirectoryLocation, source.toString()
			            .substring(sourceDirectoryLocation.length()));
			          try {
			              Files.copy(source, destination);
			          } catch (IOException e) {
			              e.printStackTrace();
			          }
			      });
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return Stream.of(p);
		}
		return Stream.of(p);
    }
}
