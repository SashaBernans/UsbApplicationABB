package app.controller;

import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import app.model.ImageCopier;
import app.view.CopyFilesView;

/**
 * @author Sasha Bernans
 *This class communicates with the copyFilesView and the imageCopier
 */
public class CopyFilesController implements PropertyChangeListener{
	private ImageCopier copier;
	private CopyFilesView copyFilesView;
	private MainController mainController;

	
	/**
	 * Sets copier property, executes the copier thread. and creates the loading window.
	 * @param copier
	 */
	public CopyFilesController(ImageCopier copier, MainController mainController) {
		this.copier = copier;
		copier.addPropertyChangeListener(this);
		copier.execute();
		this.copyFilesView= new CopyFilesView(this);
		this.mainController = mainController;
	}

	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(copier.isDone()){
			copyFilesView.completed();
			this.copyFilesView.dispatchEvent(new WindowEvent(copyFilesView, WindowEvent.WINDOW_CLOSING));
			this.mainController.refreshMain();
		}
	}
}
