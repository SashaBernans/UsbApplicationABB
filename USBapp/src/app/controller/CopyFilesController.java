package app.controller;

import app.image.ImageCopier;

public class CopyFilesController implements ICopyFilesController{
	private ImageCopier copier;

	public CopyFilesController(ImageCopier copier) {
		this.copier = copier;
	}

	@Override
	public void copyFiles() {
		copier.copyImageToUsb();
	}
}
