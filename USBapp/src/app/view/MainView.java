package app.view;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;

import app.controller.MainController;
import app.model.Image;
import app.model.Constants;

/**
 * @author Sasha Bernans
 *This is the main menu of the application if this window is closed the application exits.
 */
public class MainView extends JFrame implements ActionListener{
	
	private static final String FTSWAERI_SELECTED_WITH_OTHER_THAN_LAPTOP = "FTSWAERI can only be selected with laptop computer type";
	private static final String FNO_FTSW100_SELECTED_WITH_INDUSTRIAL_COMPUTER_MESSAGE = "FTSW100 must be selected with Industrial computer type";
	private static final String NO_FTSW100_SELECTED_WITH_RACK_PC_MESSAGE = "FTSW100 must be selected with Rack-PC type";
	private static final String NO_USB_SELECTED_MESSAGE = "A USB drive must be selected";
	private static final String NO_COMPUTER_SELECTED_MESSAGE = "No computer type selected.";
	private static final String FTSWAERI_BOX = "FTSWAERI";
	private static final String PANASONIC_BOX = "Panasonic";
	private static final String CANCEL_ACTION_COMMAND = "create";
	private static final String CANCEL_BUTTON_TEXT = "Cancel";
	private static final String CREATE_ACTION_COMMAND = "run";
	private static final String SETTINGS_ACTION_COMMAND = "goToSettings";
	private static final String CREATE_BUTTON_TEXT = "Create";
	private static final String SETTINGS_BUTTON_TEXT = "Settings";
	private static final String DESCRIPTION_LABEL = "Description (Serial Number, Site Name, Special Revision, etc...):";
	private static final String CUSTOMER_NAME_LABEL = "Customer's name (Optional):";
	private static final String WORK_ORDER_LABEL = "Work Order:";
	private static final String SALES_ORDER_LABEL = "Sales Order:";
	private static final String CUSTOMER_INFORMATION_TITLE = "Customer Informations:";
	private static final String HO_MB_BOX = "HoMB";
	private static final String HO_QA_BOX = "HoQA";
	private static final String FTSW100_BOX = "FTSW100";
	private static final String INDUSTRIAL_COMPUTER_BOX = "Industrial Computer";
	private static final String RACK_PC_BOX = "Rack PC";
	private static final String LAPTOP_BOX = "Laptop";
	private static final String DESKTOP_BOX = "Desktop";
	private static final String SELECT_SOFTWARE_MESSAGE = "Select software(s):";
	private static final String SELECT_COMPUTER_MESSAGE = "Select computer type:";
	private static final String SELECT_USB_DRIVE_MESSAGE = "Select USB drive:";
	private static final String APPLICATION_NAME = "USB Disk Creator";
	private static final int WINDOW_HEIGHT = 700;
	private static final int WINDOW_WIDTH = 550;
	private MainController controller;
	private CheckboxGroup computerTypes = new CheckboxGroup();
	private CheckboxGroup softwareTypes = new CheckboxGroup();
	private Checkbox FTW100_Box = new Checkbox(FTSW100_BOX,softwareTypes,false);
	private Checkbox HoQA_Box = new Checkbox(HO_QA_BOX,softwareTypes,false);
	private Checkbox FTSWAERI_Box = new Checkbox(FTSWAERI_BOX,softwareTypes,false);
	private Checkbox HoMB_Box = new Checkbox(HO_MB_BOX);
	private JTextField salesOrderInput =new JTextField();
	private JTextField workOrderInput =new JTextField();
	private JTextField customerNameInput =new JTextField();
	private JTextField descriptionInput =new JTextField();
	private Checkbox desktopBox = new Checkbox(DESKTOP_BOX,computerTypes,false);
	private Checkbox laptopBox = new Checkbox(LAPTOP_BOX,computerTypes,false);
	private Checkbox rackPCBox = new Checkbox(RACK_PC_BOX,computerTypes,false);
	private Checkbox industrialComputorBox = new Checkbox(INDUSTRIAL_COMPUTER_BOX,computerTypes,false);
	private Checkbox panasonicBox = new Checkbox(PANASONIC_BOX,computerTypes,false);
	private JButton settingsButton = new JButton(SETTINGS_BUTTON_TEXT);
	private JButton createButton = new JButton(CREATE_BUTTON_TEXT);
	private JButton cancelButton = new JButton(CANCEL_BUTTON_TEXT);
	private JComboBox<File> usbDropDownList = new JComboBox<File>();

	/**
	 * This JFrame must be given a controller to communicate with other classes.
	 * @param controller
	 */
	public MainView(MainController controller) {
		super(APPLICATION_NAME); // sets application name.
		
		this.controller = controller;
		
		this.initialize();
		this.setUpComponents();
	}
	
	/**
	 * Sets this JFrame's default properties
	 */
	private void initialize() {
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setLayout(new GridLayout(2,1,0,0)); // 3 rows 1 column

		//exits app on close window action
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	
	/**
	 * Adds Panels to this JFrame
	 */
	private void setUpComponents() {
		this.setUsbDrives();
		this.setUpSelectionPanel();
		this.setUpCustomerInformationPanel();
	}

	/**
	 * Adds a panel with action buttons to the customerInformationPanel (Create, Settings, Cancel)
	 */
	private void setUpButtonPanel(JPanel customerInformationPanel) {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,3,10,0));
		customerInformationPanel.add(buttonPanel);
		
		//Add this class as an actionListener to the buttons and sets action command.
		this.settingsButton.addActionListener(this);
		this.settingsButton.setActionCommand(SETTINGS_ACTION_COMMAND);
		this.createButton.addActionListener(this);
		this.createButton.setActionCommand(CREATE_ACTION_COMMAND);
		this.cancelButton.addActionListener(this);
		this.cancelButton.setActionCommand(CANCEL_ACTION_COMMAND);
		
		buttonPanel.add(this.settingsButton);
		buttonPanel.add(this.createButton);
		buttonPanel.add(this.cancelButton);
	}

	/**
	 * Adds the panel to select usb drive, computer type and software.
	 */
	private void setUpSelectionPanel() {
		JPanel selectionPanel = new JPanel();
		//creates gap of 10 pixels between JFrame border and this selectionPanel
		selectionPanel.setBorder(BorderFactory.createCompoundBorder(
		    BorderFactory.createEmptyBorder(10, 10, 10, 10), // outer border
		    BorderFactory.createLoweredBevelBorder()));      // inner border
		
		selectionPanel.setLayout(new GridLayout(2,2,0,0)); //2 rows 2 columns
		this.add(selectionPanel);
		
		JLabel selectUsbMessage = new JLabel(SELECT_USB_DRIVE_MESSAGE);
		selectUsbMessage.setFont(new Font("Serif", Font.BOLD, 16));
		
		selectionPanel.add(selectUsbMessage);
		selectionPanel.add(this.usbDropDownList);
		
		//Creates gap between Panel border and usbDropDownList
		this.usbDropDownList.setBorder(BorderFactory.createCompoundBorder(
		    BorderFactory.createEmptyBorder(55, 11, 55, 11), // outer border
		    BorderFactory.createLoweredBevelBorder()));// inner border
		
		this.setUpComputerTypePanel(selectionPanel);
		this.setUpSoftwarePanel(selectionPanel);
	}

	/**
	 * This method detects the usb drives and adds the letters to the dropDownList.
	 */
	private void setUsbDrives() {
		File[] paths;
		String driveType;
		// Gets all drives
		paths = File.listRoots();
		
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// for each pathname in pathname array
		for(File path:paths)
		{
		    // add file to comboBox if file is usb Drive
			driveType = fsv.getSystemTypeDescription(path);
			if(driveType.equals("USB Drive")) {
				this.usbDropDownList.addItem(path);
			}
		}
	}

	/**
	 * This method adds the input fields for the user to fill out (sales order, work order, customer name, description).
	 */
	private void setUpCustomerInformationPanel() {
		JPanel customerInformationPanel = new JPanel();
		//creates gap of 10 pixels between JFrame border and this selectionPanel
		customerInformationPanel.setBorder(BorderFactory.createCompoundBorder(
		    BorderFactory.createEmptyBorder(10, 10, 10, 10), // outer border
		    BorderFactory.createLoweredBevelBorder()));      // inner border
		customerInformationPanel.setLayout(new GridLayout(10,1,0,5));
		this.add(customerInformationPanel);
		
		JLabel customerInformationTitle = new JLabel(CUSTOMER_INFORMATION_TITLE, SwingConstants.CENTER);
		customerInformationTitle.setFont(new Font("Serif", Font.BOLD, 16));
		customerInformationPanel.add(customerInformationTitle);
		
		JLabel salesOrderLabel= new JLabel(SALES_ORDER_LABEL);
		salesOrderLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		customerInformationPanel.add(salesOrderLabel);
		
		customerInformationPanel.add(this.salesOrderInput);
		
		JLabel workOrderLabel= new JLabel(WORK_ORDER_LABEL);
		workOrderLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		customerInformationPanel.add(workOrderLabel);
		
		customerInformationPanel.add(this.workOrderInput);
		
		JLabel customerNameLabel= new JLabel(CUSTOMER_NAME_LABEL);
		customerNameLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		customerInformationPanel.add(customerNameLabel);
		
		customerInformationPanel.add(this.customerNameInput);
		
		JLabel descriptionLabel= new JLabel(DESCRIPTION_LABEL);
		descriptionLabel.setFont(new Font("Serif", Font.PLAIN, 14));
		customerInformationPanel.add(descriptionLabel);
		
		customerInformationPanel.add(this.descriptionInput);
		this.setUpButtonPanel(customerInformationPanel);
	}

	/**
	 * This method adds the software selection to the selection panel.
	 * @param selectionPanel
	 */
	private void setUpSoftwarePanel(JPanel selectionPanel) {
		JPanel softwarePanel = new JPanel();
		softwarePanel.setLayout(new GridLayout(5,1,0,0)); // 5 rows 1 column
		selectionPanel.add(softwarePanel);
		
		JLabel selectSoftwareMessage = new JLabel(SELECT_SOFTWARE_MESSAGE);
		selectSoftwareMessage.setFont(new Font("Serif", Font.BOLD, 16));
		softwarePanel.add(selectSoftwareMessage);
		
		softwarePanel.add(this.FTW100_Box);
		softwarePanel.add(this.HoQA_Box);
		softwarePanel.add(this.FTSWAERI_Box);
		softwarePanel.add(this.HoMB_Box);
	}

	/**
	 * This method adds the computer type selection to the selection panel.
	 * @param selectionPanel
	 */
	private void setUpComputerTypePanel(JPanel selectionPanel) {
		JPanel computerTypePanel = new JPanel();
		computerTypePanel.setLayout(new GridLayout(6,1,0,0)); // 6 rows 1 column
		selectionPanel.add(computerTypePanel);
		
		JLabel selectComputerMessage = new JLabel(SELECT_COMPUTER_MESSAGE);
		selectComputerMessage.setFont(new Font("Serif", Font.BOLD, 16));
		computerTypePanel.add(selectComputerMessage);
		
		computerTypePanel.add(this.desktopBox);
		computerTypePanel.add(this.laptopBox);
		computerTypePanel.add(this.rackPCBox);
		computerTypePanel.add(this.industrialComputorBox);
		computerTypePanel.add(this.panasonicBox);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case SETTINGS_ACTION_COMMAND:
			JPasswordField passwordInput = new JPasswordField();
			int result = JOptionPane.showConfirmDialog(this, passwordInput, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			String password  = new String(passwordInput.getPassword());
			if(result==JOptionPane.OK_OPTION) {
				if(password.equals(Constants.SETTINGS_PASSWORD)) {
					this.controller.goToSettings();
				}
			}
			break;
		case CREATE_ACTION_COMMAND:
			this.validateUserInputs();
			break;
		case CANCEL_ACTION_COMMAND:
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}

	/**
	 *  This method validates that the user inputs are respecting the constraints.
	 */
	private void validateUserInputs() {
		int result =JOptionPane.showConfirmDialog(this,
			    "Are you sure you want to proceed?",
			    "WARNING",
			    JOptionPane.YES_NO_OPTION);
		if(result==0) {
			//Prevents FTSWAERI to be selected without laptop type and notifies user.
			if(!this.laptopBox.getState()&& this.FTSWAERI_Box.getState()) {
				this.alertUser(FTSWAERI_SELECTED_WITH_OTHER_THAN_LAPTOP);
			}
			//Prevents Industrial computer type to be selected without FTSW100 and notifies user.
			else if(this.industrialComputorBox.getState() && !this.FTW100_Box.getState()) {
				this.alertUser(FNO_FTSW100_SELECTED_WITH_INDUSTRIAL_COMPUTER_MESSAGE);
			}
			//Prevents Rack-PC type to be selected without FTSW100 and notifies user.
			else if(this.rackPCBox.getState() && !this.FTW100_Box.getState()) {
				this.alertUser(NO_FTSW100_SELECTED_WITH_RACK_PC_MESSAGE);
			}
			//Prevents creation if no USB drive is selected.
			else if(this.usbDropDownList.getSelectedItem()==null) {
				this.alertUser(NO_USB_SELECTED_MESSAGE);
			}
			else if(this.desktopBox.getState()){
				this.createDesktopImage();
			}
			else if(this.laptopBox.getState()) {
				this.createLaptopImage();
			}
			else if(this.industrialComputorBox.getState()) {
				this.createIndustrialComputerImage();
			}
			else if(this.rackPCBox.getState()) {
				this.createRackPCImage();
			}
			else if(this.panasonicBox.getState()) {
				this.createPanasonicImage();
			}
			//Prevents creation if no computer type is selected and notifies user.
			else{
				this.alertUser(NO_COMPUTER_SELECTED_MESSAGE);
			}
		}
	}
	
	/**
	 * This disables buttons in this JFrame.
	 */
	public void disableButtons() {
		this.createButton.setEnabled(false);
		this.settingsButton.setEnabled(false);
		this.cancelButton.setEnabled(false);
	}

	/**
	 * Creates panasonic image object and sends it too the controller.
	 */
	private void createPanasonicImage() {
		Image image = new Image(
				Constants.PANASONIC,
				this.getSoftwareFolderNames(),
				this.workOrderInput.getText(),
				this.salesOrderInput.getText(),
				this.customerNameInput.getText(),
				this.descriptionInput.getText()
				);
		controller.goToCopyFilesView(image, usbDropDownList.getSelectedItem().toString());
	}

	/**
	 * Creates Rack pc image object and sends it too the controller.
	 */
	private void createRackPCImage() {
		Image image = new Image(
				Constants.RACK_PC,
				this.getSoftwareFolderNames(),
				this.workOrderInput.getText(),
				this.salesOrderInput.getText(),
				this.customerNameInput.getText(),
				this.descriptionInput.getText()
				);
		controller.goToCopyFilesView(image, usbDropDownList.getSelectedItem().toString());
	}

	/**
	 * Creates Industrial computer image object and sends it too the controller.
	 */
	private void createIndustrialComputerImage() {
		Image image = new Image(
				Constants.INDUSTRIAL_COMPUTER,
				this.getSoftwareFolderNames(),
				this.workOrderInput.getText(),
				this.salesOrderInput.getText(),
				this.customerNameInput.getText(),
				this.descriptionInput.getText()
				);
		controller.goToCopyFilesView(image, usbDropDownList.getSelectedItem().toString());
	}

	/**
	 * Creates laptop image object and sends it too the controller.
	 */
	private void createLaptopImage() {
		String TIBPath = null;
		if(this.FTSWAERI_Box.getState()) {
			TIBPath = Constants.LAPTOP_FOR_AERI;
		}
		else if(this.FTW100_Box.getState()){
			TIBPath = Constants.LAPTOP_WITH_FTSW100;
		}
		else {
			TIBPath = Constants.LAPTOP_WITHOUT_FTSW100;
		}
		Image image = new Image(
				TIBPath,
				this.getSoftwareFolderNames(),
				this.workOrderInput.getText(),
				this.salesOrderInput.getText(),
				this.customerNameInput.getText(),
				this.descriptionInput.getText()
				);
		controller.goToCopyFilesView(image, usbDropDownList.getSelectedItem().toString());
	}

	/**
	 * Creates desktop image object and sends it too the controller.
	 */
	private void createDesktopImage() {
		String TIBPath = null;
		if(this.FTW100_Box.getState()){
			TIBPath = Constants.DESKTOP_WITH_FTSW100;
		}
		else {
			TIBPath = Constants.DESKTOP_WITHOUT_FTSW100;
		}
		Image image = new Image(
				TIBPath,
				this.getSoftwareFolderNames(),
				this.workOrderInput.getText(),
				this.salesOrderInput.getText(),
				this.customerNameInput.getText(),
				this.descriptionInput.getText()
				);
		controller.goToCopyFilesView(image, usbDropDownList.getSelectedItem().toString());
	}
	
	/**
	 * @return the start of the right softwares directories selected by the user.
	 */
	private ArrayList<String> getSoftwareFolderNames() {
		ArrayList<String> softwarePaths = new ArrayList<String>();
		if(this.FTSWAERI_Box.getState()) {
			softwarePaths.add(Constants.FTSWAERI);
		}
		if(this.FTW100_Box.getState()) {
			softwarePaths.add(Constants.FTSW100);
		}
		if(this.HoMB_Box.getState()) {
			softwarePaths.add(Constants.HOMB);
		}
		if(this.HoQA_Box.getState()) {
			softwarePaths.add(Constants.HOQA);
		}
		return softwarePaths;
	}

	public void display() {
		this.setVisible(true);
	}

	/**
	 * This method alerts the user with a pop up error message.
	 * @param error : message that will be displayed on pop up.
	 */
	public void alertUser(String error) {
		if(error!=null) {
		JOptionPane.showMessageDialog(this,
			    "FAILED -"+error,
			    "ERROR",
			    JOptionPane.WARNING_MESSAGE);
		}
	}
}
