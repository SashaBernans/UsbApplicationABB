package app.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import app.model.ImageCopier;
import app.view.CopyFilesView;

public class CopyFilesController implements ICopyFilesController, PropertyChangeListener{
	private ImageCopier copier;
	private CopyFilesView copyFilesView;

	
	/**
	 * Sets copier property, executes the copier thread. and creates the loading window.
	 * @param copier
	 */
	public CopyFilesController(ImageCopier copier) {
		this.copier = copier;
		copier.addPropertyChangeListener(this);
		copier.execute();
		this.copyFilesView= new CopyFilesView(this);
	}

	/**
	 *This method is called when ImageCopier.doInBackground() thread is finished computing.
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(copier.isDone()){
			copyFilesView.completed();
		}
	}
}
