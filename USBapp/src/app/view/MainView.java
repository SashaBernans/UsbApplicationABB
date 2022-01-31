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
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileSystemView;

import app.controller.IMainController;
import app.image.Image;
import app.image.ImageConstants;

public class MainView extends JFrame implements IView, ActionListener{
	
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
	private static final int WINDOW_HEIGHT = 600;
	private static final int WINDOW_WIDTH = 500;
	private IMainController controller;
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

	public MainView(IMainController controller) {
		super(APPLICATION_NAME);
		
		this.controller = controller;
		
		this.initialize();
		this.setUpComponents();
	}
	
	private void initialize() {
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setLayout(new GridLayout(3,1,0,0));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setUpComponents() {
		this.setUsbDrives();
		this.setUpSelectionPanel();
		this.setUpCustomerInformationPanel();
		this.setUpButtonPanel();
	}

	private void setUpButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,70,70));
		this.add(buttonPanel);
		
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

	private void setUpSelectionPanel() {
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new GridLayout(2,2,0,0));
		this.add(selectionPanel);
		
		JLabel selectUsbMessage = new JLabel(SELECT_USB_DRIVE_MESSAGE);
		selectUsbMessage.setFont(new Font("Serif", Font.BOLD, 16));
		selectionPanel.add(selectUsbMessage);
	
		selectionPanel.add(this.usbDropDownList);
		
		this.setUpComputerTypePanel(selectionPanel);
		this.setUpSoftwarePanel(selectionPanel);
	}

	private void setUsbDrives() {
		File[] paths;
		String driveType;
		// returns pathnames for files and directory
		paths = File.listRoots();
		
		FileSystemView fsv = FileSystemView.getFileSystemView();
		// for each pathname in pathname array
		for(File path:paths)
		{
		    // add file to comboBox
			driveType = fsv.getSystemTypeDescription(path);
			if(driveType.equals("USB Drive")) {
				this.usbDropDownList.addItem(path);
			}
		}
	}

	private void setUpCustomerInformationPanel() {
		JPanel customerInformationPanel = new JPanel();
		customerInformationPanel.setLayout(new GridLayout(9,1,0,0));
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
	}

	private void setUpSoftwarePanel(JPanel selectionPanel) {
		JPanel softwarePanel = new JPanel();
		softwarePanel.setLayout(new GridLayout(5,1,0,0));
		selectionPanel.add(softwarePanel);
		
		JLabel selectSoftwareMessage = new JLabel(SELECT_SOFTWARE_MESSAGE);
		selectSoftwareMessage.setFont(new Font("Serif", Font.BOLD, 16));
		softwarePanel.add(selectSoftwareMessage);
		
		softwarePanel.add(this.FTW100_Box);
		softwarePanel.add(this.HoQA_Box);
		softwarePanel.add(this.FTSWAERI_Box);
		softwarePanel.add(this.HoMB_Box);
	}

	private void setUpComputerTypePanel(JPanel selectionPanel) {
		JPanel computerTypePanel = new JPanel();
		computerTypePanel.setLayout(new GridLayout(6,1,0,0));
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
			this.controller.goToSettings();
			break;
		case CREATE_ACTION_COMMAND:
			this.createImage();
			break;
		case CANCEL_ACTION_COMMAND:
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}

	private void createImage() {
		//Prevents FTSWAERI to be selected without laptop type
		if(!this.laptopBox.getState()&& this.FTSWAERI_Box.getState()) {
			JOptionPane.showMessageDialog(this,
				    "FAILED -FTSWAERI can only be selected with laptop computer type",
				    "ERROR",
				    JOptionPane.WARNING_MESSAGE);
		}
		else if(this.industrialComputorBox.getState() && !this.FTW100_Box.getState()) {
			JOptionPane.showMessageDialog(this,
				    "FAILED -FTSW100 must be selected with Industrial computer type",
				    "ERROR",
				    JOptionPane.WARNING_MESSAGE);
		}
		else if(this.rackPCBox.getState() && !this.FTW100_Box.getState()) {
			JOptionPane.showMessageDialog(this,
				    "FAILED -FTSW100 must be selected with Rack-PC type",
				    "ERROR",
				    JOptionPane.WARNING_MESSAGE);
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
		else{
			JOptionPane.showMessageDialog(this,
				    "FAILED -No computer type selected.",
				    "ERROR",
				    JOptionPane.WARNING_MESSAGE);
		}
	}
	
	private void createPanasonicImage() {
		Image image = new Image(
				ImageConstants.PANASONIC,
				this.getSoftwareFolderNames(),
				this.workOrderInput.getText(),
				this.salesOrderInput.getText(),
				this.customerNameInput.getText(),
				this.descriptionInput.getText()
				);
		controller.createImage(image, usbDropDownList.getSelectedItem().toString());
	}

	private void createRackPCImage() {
		Image image = new Image(
				ImageConstants.RACK_PC,
				this.getSoftwareFolderNames(),
				this.workOrderInput.getText(),
				this.salesOrderInput.getText(),
				this.customerNameInput.getText(),
				this.descriptionInput.getText()
				);
		controller.createImage(image, usbDropDownList.getSelectedItem().toString());
	}

	private void createIndustrialComputerImage() {
		Image image = new Image(
				ImageConstants.INDUSTRIAL_COMPUTER,
				this.getSoftwareFolderNames(),
				this.workOrderInput.getText(),
				this.salesOrderInput.getText(),
				this.customerNameInput.getText(),
				this.descriptionInput.getText()
				);
		controller.createImage(image, usbDropDownList.getSelectedItem().toString());
	}

	private void createLaptopImage() {
		String TIBPath = null;
		if(this.FTSWAERI_Box.getState()) {
			TIBPath = ImageConstants.LAPTOP_FOR_AERI;
		}
		else if(this.FTW100_Box.getState()){
			TIBPath = ImageConstants.LAPTOP_WITH_FTSW100;
		}
		else {
			TIBPath = ImageConstants.LAPTOP_WITHOUT_FTSW100;
		}
		Image image = new Image(
				TIBPath,
				this.getSoftwareFolderNames(),
				this.workOrderInput.getText(),
				this.salesOrderInput.getText(),
				this.customerNameInput.getText(),
				this.descriptionInput.getText()
				);
		controller.createImage(image, usbDropDownList.getSelectedItem().toString());
	}

	private void createDesktopImage() {
		String TIBPath = null;
		if(this.FTW100_Box.getState()){
			TIBPath = ImageConstants.DESKTOP_WITH_FTSW100;
		}
		else {
			TIBPath = ImageConstants.DESKTOP_WITHOUT_FTSW100;
		}
		Image image = new Image(
				TIBPath,
				this.getSoftwareFolderNames(),
				this.workOrderInput.getText(),
				this.salesOrderInput.getText(),
				this.customerNameInput.getText(),
				this.descriptionInput.getText()
				);
		controller.createImage(image, usbDropDownList.getSelectedItem().toString());
	}
	
	private ArrayList<String> getSoftwareFolderNames() {
		ArrayList<String> softwarePaths = new ArrayList<String>();
		if(this.FTSWAERI_Box.getState()) {
			softwarePaths.add(ImageConstants.FTSWAERI);
		}
		if(this.FTW100_Box.getState()) {
			softwarePaths.add(ImageConstants.FTSW100);
		}
		if(this.HoMB_Box.getState()) {
			softwarePaths.add(ImageConstants.HOMB);
		}
		if(this.HoQA_Box.getState()) {
			softwarePaths.add(ImageConstants.HOQA);
		}
		return softwarePaths;
	}

	@Override
	public void display() {
		this.setVisible(true);
	}
}
