package app.controller;

import app.model.Image;
import app.model.ImageCopier;
import app.view.IView;
import app.view.MainView;

public class MainController implements IMainController {
	
	private static final int WORK_ORDER_MINIMUM_LENTGH = 7;
	private static final int SALES_ORDER_REQUIRED_LENGTH = 6;
	private ImageCopier imageCopier;
	private ICopyFilesController copyFilesController;
	private MainView mainView;
	
	public MainController() {
	}

	/**
	 *Displays the mainMenu
	 */
	@Override
	public void startApplication() {
		this.mainView = new MainView(this);
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
	public void goToCopyFilesView(Image image, String usbPath) {
		if(this.customerInformationIsValid(image, usbPath)) {
			this.mainView.disableButtons();
			this.imageCopier = new ImageCopier(image,usbPath);
			this.copyFilesController = new CopyFilesController(this.imageCopier);
		}
	}

	/**
	 *Validates the user information inputs
	 */
	private boolean customerInformationIsValid(Image image, String usbPath) {
		String error = null;
		boolean isValid = true;
		//verify sales order character number
		if(image.getSalesOrder().length()!=SALES_ORDER_REQUIRED_LENGTH) {
			error = "Sales order must be 6 characters long";
			isValid = false;
		}
		if(image.getWorkOrder().length()<WORK_ORDER_MINIMUM_LENTGH) {
			error = "Work order must be at least 7 characters long";
			isValid = false;
		}
		if(!image.getWorkOrder().matches("\\d+")) {
			error = "Work order can only be numbers or -";
			isValid = false;
		}
		this.mainView.alertUser(error);
		return isValid;
	}

}
