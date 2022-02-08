package app.controller;

import app.model.Image;
import app.model.ImageCopier;
import app.view.IView;
import app.view.MainView;

public class MainController implements IMainController {
	
	private ImageCopier imageCopier;
	private ICopyFilesController copyFilesController;
	
	public MainController() {
	}

	/**
	 *Displays the mainMenu
	 */
	@Override
	public void startApplication() {
		IView mainView = new MainView(this);
		mainView.display();
	}

	@Override
	public void goToSettings() {
		System.out.println("no settings yet");
	}

	/**
	 *Creates the image copier and gives it the image to copy, then creates the controller to communicate
	 *with copyFilesView.
	 */
	@Override
	public void createImage(Image image, String usbPath) {
		this.imageCopier = new ImageCopier(image,usbPath);
		this.copyFilesController = new CopyFilesController(this.imageCopier);
	}

}
