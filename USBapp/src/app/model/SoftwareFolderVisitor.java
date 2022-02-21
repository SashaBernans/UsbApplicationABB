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
	
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
	   if (!Files.isReadable(dir)) {
	        return SKIP_SUBTREE;
	   }
	   return CONTINUE;
	}
	
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
