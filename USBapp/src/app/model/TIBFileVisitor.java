package app.model;

import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.*;

import java.awt.Image;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;

public class TIBFileVisitor extends SimpleFileVisitor<Path>{
	
	private ImageCopier imageCopier;

	public TIBFileVisitor(ImageCopier imageCopier) {
		this.imageCopier = imageCopier;
	}
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
	   if (!Files.isReadable(dir)) {
	        return SKIP_SUBTREE;
	   }
	   return CONTINUE;
	}
	
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
		if(file.getFileName().toString().startsWith(imageCopier.getImage().getTIBName())) {
			this.imageCopier.setTIBPath(file.toString());
			return TERMINATE;
		}
		return CONTINUE;
	}
}
