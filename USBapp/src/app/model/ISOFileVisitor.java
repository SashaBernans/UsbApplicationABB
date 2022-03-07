package app.model;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.FileVisitResult.TERMINATE;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class ISOFileVisitor extends SimpleFileVisitor<Path>{
	private ImageCopier imageCopier;

	public ISOFileVisitor(ImageCopier imageCopier) {
		this.imageCopier = imageCopier;
	}
	
	/**
	 *This checks if a the directory is readable before visiting it. If the dir is
	 *not readable the fileVisitor will skip it and it's entire subtree. If the dir starts with the
	 *the ISO part number then it is set in imageCopier.
	 */
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
	   if (Files.isReadable(dir)) {
		   if(dir.getFileName().toString().startsWith(Constants.ISO)) {
			   	File[] contents =dir.toFile().listFiles();
			   	for(int i=0; i<contents.length;i++) {
			   		if(contents[i].getName().toString().startsWith(Constants.ISO)) {
			   			this.imageCopier.setISOPath(contents[i].toString());
						System.out.println("FileVisitorISODir : "+dir.toString());
						return TERMINATE;
			   		}
			   	}
			}
	   }
	   else {
		   return SKIP_SUBTREE;
	   }
	   return CONTINUE;
	}
}
