package app.model;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class SoftwareFolderVisitor extends SimpleFileVisitor<Path>{
	private ImageCopier imageCopier;

	public SoftwareFolderVisitor(ImageCopier imageCopier) {
		this.imageCopier = imageCopier;
	}
	
	/**
	 *This checks if a the directory is readable before visiting it. If the dir is
	 *not readable the fileVisitor will skip it and it's entire subtree.
	 */
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
	   if (!Files.isReadable(dir)) {
	        return SKIP_SUBTREE;
	   }
	   return CONTINUE;
	}
	
	
	/**
	 *After visiting a directory this checks if it starts with one of the right part numbers.
	 *If so, it is added to the imageCopier's director
	 */
	@Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		ArrayList<String> softwareNames = imageCopier.getImage().getSoftwareFolderNames();
		for(int i=0;i<softwareNames.size();i++) {
	        if(dir.getFileName().toString().startsWith(softwareNames.get(i))) {
	        	imageCopier.addDirectoryToCopy(dir.toString());
	        }
		}
        return CONTINUE;
    }
}
