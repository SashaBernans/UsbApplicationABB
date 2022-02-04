package app.controller;

import app.image.Image;
import app.image.ImageCopier;
import app.image.UsbFormatter;
import app.view.CopyFilesView;
import app.view.IView;
import app.view.MainView;

public class MainController implements IMainController {
	
	private ImageCopier imageCopier;
	private UsbFormatter usbFormatter;
	private ICopyFilesController copyFilesController;
	
	public MainController() {
		this.usbFormatter = new UsbFormatter();
	}

	@Override
	public void startApplication() {
		IView mainView = new MainView(this);
		mainView.display();
	}

	@Override
	public void goToSettings() {
		// TODO Auto-generated method stub
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaa");
	}

	@Override
	public void createImage(Image image, String usbPath) {
		ImageCopier copier = new ImageCopier(image,usbPath);
		ICopyFilesController filesController = new CopyFilesController(copier);
		IView copyView = new CopyFilesView(filesController);
	}

	@Override
	public void formatDrive(String drive, String workOrder) {
		this.usbFormatter.formatToNTFS(drive, workOrder);
	}

}
