package app.controller;

import app.image.Image;
import app.image.ImageCopier;
import app.image.UsbFormatter;
import app.view.IView;
import app.view.MainView;

public class MainController implements IMainController {
	
	private ImageCopier imageCopier;
	private UsbFormatter usbFormatter;
	
	public MainController() {
		this.imageCopier = new ImageCopier();
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
			this.imageCopier.copyImageToUsb(image, usbPath);
	}

	@Override
	public void formatDrive(String drive) {
		this.usbFormatter.formatToNTFS(drive);
	}

}
