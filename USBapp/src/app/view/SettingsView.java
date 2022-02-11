package app.view;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import app.config.ConfigManager;
import app.controller.SettingsController;

public class SettingsView extends JFrame implements ActionListener{

	private static final String CURRENT_DEFAULT_PATH_LABEL = "Current default path : ";
	private static final String NEW_DEFAULT_PATH_LABEL = "Enter new default path :";
	private static final String SAVE_ACTION_COMMAND = "save";
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 250;
	private static final String BACK_ACTION_COMMAND = "back";
	private SettingsController controller;
	private JTextField newDefaultPathInput =new JTextField();
	private JButton saveButton = new JButton(SAVE_ACTION_COMMAND);
	private JButton backButton = new JButton(BACK_ACTION_COMMAND);
	private JLabel currentPath;

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
		JPanel panel = new JPanel(new GridLayout(3,1,0,0));
		//creates gap of 10 pixels between JFrame border and this panel
		panel.setBorder(BorderFactory.createCompoundBorder(
		    BorderFactory.createEmptyBorder(10, 10, 10, 10), // outer border
		    BorderFactory.createLoweredBevelBorder()));      // inner border
		
		this.currentPath = new JLabel(CURRENT_DEFAULT_PATH_LABEL+ConfigManager.getString("defaultPath"));
		JLabel newDefaultPathLabel = new JLabel(NEW_DEFAULT_PATH_LABEL);
		
		panel.add(newDefaultPathLabel);
		panel.add(newDefaultPathInput);
		panel.add(currentPath);
		this.add(panel);
	}
	
	public void display() {
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
		case SAVE_ACTION_COMMAND:
			if(JOptionPane.showConfirmDialog(this,
					"Are you sure you want to change the default path?",
					"CONFIRMATION",
					JOptionPane.YES_NO_OPTION)==0){
				this.controller.saveSettings(this.newDefaultPathInput.getText());
				this.currentPath.setText(CURRENT_DEFAULT_PATH_LABEL+ConfigManager.getString("defaultPath"));
			}
			break;
		case BACK_ACTION_COMMAND:
			JOptionPane.showConfirmDialog(null, ConfigManager.getString("defaultPath"));
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
}
