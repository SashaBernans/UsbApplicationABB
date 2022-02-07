package app.controller;

import app.image.Image;
import app.image.ImageCopier;
import app.view.IView;
import app.view.MainView;

public class MainController implements IMainController {
	
	private ImageCopier imageCopier;
	private ICopyFilesController copyFilesController;
	
	public MainController() {
	}

	@Override
	public void startApplication() {
		IView mainView = new MainView(this);
		mainView.display();
	}

	@Override
	public void goToSettings() {
		System.out.println("no settings yet");
	}

	@Override
	public void createImage(Image image, String usbPath) {
		this.imageCopier = new ImageCopier(image,usbPath);
		this.copyFilesController = new CopyFilesController(this.imageCopier);
	}

}
