package app.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import app.image.ImageCopier;
import app.view.CopyFilesView;

public class CopyFilesController implements ICopyFilesController, PropertyChangeListener{
	private ImageCopier copier;
	private CopyFilesView copyFilesView;

	public CopyFilesController(ImageCopier copier) {
		this.copier = copier;
		this.copyFilesView= new CopyFilesView(this);
	}

	@Override
	public void copyFiles() {
		copier.addPropertyChangeListener(this);
		copier.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(copier.isDone()){
			copyFilesView.completed();
		}
	}
}
