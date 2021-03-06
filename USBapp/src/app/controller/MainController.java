package app.controller;

import app.model.Image;
import app.model.ImageCopier;
import app.view.MainView;

/**
 * @author Sasha Bernans
 *This class communicates between the MainView and the other controllers. It also does basic input validation for the mainView.
 */
public class MainController {
	
	private static final int WORK_ORDER_MINIMUM_LENTGH = 7;
	private static final int SALES_ORDER_REQUIRED_LENGTH = 6;
	private ImageCopier imageCopier;
	private CopyFilesController copyFilesController;
	private MainView mainView;
	private SettingsController settings;
	
	public MainController() {
	}

	/**
	 *Displays the mainMenu
	 */
	public void startApplication() {
		this.mainView = new MainView(this);
		mainView.display();
	}

	/**
	 * Creates the settings controller that will then create the settings view
	 */
	public void goToSettings() {
		this.settings = new SettingsController();
	}

	/**
	 * This creates a ImageCopier and the controller that will create copyFilesView.
	 * @param image : image object to be passed to the imageCopier
	 * @param usbPath : the drive path to be passed to the imageCopier
	 */
	public void goToCopyFilesView(Image image, String usbPath) {
		//verify customer information before continuing
		System.out.println(image.getTIBName());
		System.out.println(image.getSoftwareFolderNames().toString());
		if(this.customerInformationIsValid(image)) {
			
			//Disables buttons in mainMenu to avoid conflicts
			this.mainView.disableButtons();
			
			this.imageCopier = new ImageCopier(image,usbPath);
			this.copyFilesController = new CopyFilesController(this.imageCopier,this);
		}
	}

	
	/**
	 * Validates the customer information
	 * @param image : the image that contains the character information to verify
	 * @return true if customerInformation is valid false if not
	 */
	private boolean customerInformationIsValid(Image image) {
		String error = null;
		//verify work order character number
		if(image.getWorkOrder().length()<WORK_ORDER_MINIMUM_LENTGH) {
			error = "Work order must be at least 7 characters long";
			this.mainView.alertUser(error);
			return false;
		}
		
		String first7Chars = image.getWorkOrder().substring(0,6);
		
		//verify sales order character number
		if(image.getSalesOrder().length()!=SALES_ORDER_REQUIRED_LENGTH) {
			error = "Sales order must be 6 characters long";
			this.mainView.alertUser(error);
			return false;
		}
		//verify work order first 7 chars are only numbers
		if(!first7Chars.matches("\\d+")) {
			error = "Work order can only be numbers or -";
			this.mainView.alertUser(error);
			return false;
		}
		return true;
	}

	public void refreshMain() {
		this.mainView.dispose();
		this.mainView = new MainView(this);
		mainView.display();
	}
}
