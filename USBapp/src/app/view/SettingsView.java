package app.view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import app.model.Constants;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

import app.config.ConfigManager;
import app.controller.SettingsController;

/**
 * This JFrame shows the default path to the user and the user can change it.
 * @author CASABER
 *
 */
public class SettingsView extends JFrame implements ActionListener{

	private static final String DEFAULT_PATH_KEY = "defaultPath";
	private static final String CHANGE_PATH_CONFIRMATION = "Are you sure you want to change the default path?";
	private static final String RELAUNCH_MESSAGE = "Relaunch application for settings to take effect : ";
	private static final String CURRENT_DEFAULT_PATH_LABEL = "Current default path : ";
	private static final String SAVE_ACTION_COMMAND = "save";
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 200;
	private static final String BACK_ACTION_COMMAND = "back";
	private SettingsController controller;
	private JButton saveButton = new JButton("Change path");
	private JButton backButton = new JButton(BACK_ACTION_COMMAND);
	private JLabel currentPath;
	private JFileChooser fileChooser= new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

	public SettingsView(SettingsController controller) {
		this.controller = controller;
		
		this.initialize();
		this.setUpComponents();
	}
	
	/**
	 * sets the JFrames default properties
	 */
	private void initialize() {
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setLayout(new GridLayout(2,1,0,0)); // 3 rows 1 column
		this.setResizable(false);
		
		//sets the usbIcon as a window icon
		ImageIcon icon = new ImageIcon(getClass().getResource(Constants.ICON_PATH));
		this.setIconImage(icon.getImage());
		//only close window on close window action
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Adds panels to this frame
	 */
	private void setUpComponents() {
		setUpPathPanel();
		setUpButtonPanel();
	}

	/**
	 * Adds the button Panel to the JFrame
	 */
	private void setUpButtonPanel() {
		JPanel buttonPanel= new JPanel(new FlowLayout(FlowLayout.CENTER,100,20));//100 pixels horizontal gap and 20 vertical
		this.add(buttonPanel);
		
		saveButton.addActionListener(this);
		saveButton.setActionCommand(SAVE_ACTION_COMMAND);
		buttonPanel.add(this.saveButton);
		
		backButton.addActionListener(this);
		backButton.setActionCommand(BACK_ACTION_COMMAND);
		buttonPanel.add(this.backButton);
	}

	
	/**
	 *Adds the path input and the current path to the jframe
	 */
	private void setUpPathPanel() {
		JPanel panel = new JPanel(new GridLayout(1,1,0,0));
		//creates gap of 10 pixels between JFrame border and this panel
		panel.setBorder(BorderFactory.createCompoundBorder(
		    BorderFactory.createEmptyBorder(10, 10, 10, 10), // outer border
		    BorderFactory.createLoweredBevelBorder()));      // inner border
		
		this.currentPath = new JLabel(CURRENT_DEFAULT_PATH_LABEL+ConfigManager.getString(DEFAULT_PATH_KEY));
		panel.add(currentPath);
		this.add(panel);
	}
	
	private void displayFileChooser() {
		this.add(fileChooser);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = fileChooser.showOpenDialog(this);

		//When the user press open in the file explorer
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			this.controller.saveSettings(selectedFile.getAbsolutePath());
			System.out.println(selectedFile.getAbsolutePath());
			JOptionPane.showMessageDialog(null,RELAUNCH_MESSAGE+ ConfigManager.getString(DEFAULT_PATH_KEY));
			System.exit(0);
		}
	}
	
	public void display() {
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case SAVE_ACTION_COMMAND:
			int userAwnser =JOptionPane.showConfirmDialog(this,
					CHANGE_PATH_CONFIRMATION,
					"CONFIRMATION",
					JOptionPane.YES_NO_OPTION);
			if(userAwnser==0){
				this.displayFileChooser();
			}
			break;
		case BACK_ACTION_COMMAND:
			this.dispose();
		}
	}
}
