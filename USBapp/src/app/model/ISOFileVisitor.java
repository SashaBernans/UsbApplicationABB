package app.model;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.FileVisitResult.TERMINATE;

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
	 *This checks if the visited file starts with the right part number, if so it
	 *sets it's path as the ISOPath in ImageCopier.
	 */
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
		if(file.getFileName().toString().startsWith(Constants.ISO)) {
			this.imageCopier.setISOPath(file.toString());
			return TERMINATE;
		}
		return CONTINUE;
	}
}
