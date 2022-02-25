package app.model;

import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.*;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;

public class TIBFileVisitor extends SimpleFileVisitor<Path>{
	
	private ImageCopier imageCopier;

	public TIBFileVisitor(ImageCopier imageCopier) {
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
	 *sets it's path as the TIBPath in ImageCopier.
	 */
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
		if(file.getFileName().toString().startsWith(imageCopier.getImage().getTIBName())) {
			this.imageCopier.setTIBPath(file.toString());
			return TERMINATE;
		}
		return CONTINUE;
	}
}
