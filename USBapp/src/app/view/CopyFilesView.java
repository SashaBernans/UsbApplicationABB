package app.view;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import app.controller.CopyFilesController;
import app.model.Constants;

/**
 * @author Sasha Bernans
 *
 */
public class CopyFilesView extends JFrame implements ActionListener{

	private static final String CANCEL_ACTION_COMMAND = "cancel";
	private static final String INSTALLATION_SUCCESS_MESSAGE = "Installation completed successfully.";
	private static final String SYSTEM_WARNING = "Do not turn off system this may take a few minutes";
	private static final String SPINNER_PATH = "optimized.gif";
	private static final String COPYING_FILES_MESSAGE = "Copying files to USB drive...";
	private static final int WINDOW_WIDTH = 350;
	private static final int WINDOW_HEIGHT = 150;
	private CopyFilesController controller;
	private JButton cancelButton = new JButton("Cancel");

	/**
	 * @param filesController : this will communicate with the imageCopier
	 */
	public CopyFilesView(CopyFilesController filesController) {
		this.controller = filesController;
		
		this.initialize();
		this.setUpComponents();
		this.display();
	}

	/**
	 * Sets up text and images in the frame
	 */
	private void setUpComponents() {
		//sets the usbIcon as a window icon
		ImageIcon icon = new ImageIcon(getClass().getResource(Constants.ICON_PATH));
		this.setIconImage(icon.getImage());
		//Adds loading gif image to the frame.
		ImageIcon loading = new ImageIcon(getClass().getResource(SPINNER_PATH));
		JLabel gif = new JLabel(COPYING_FILES_MESSAGE, loading, JLabel.CENTER);
		
		//Adds warning to not turn system off.
		JLabel warning = new JLabel(SYSTEM_WARNING, JLabel.CENTER);
		warning.setFont(new Font("Serif",Font.PLAIN,14));
	    this.add(gif);
	    this.add(warning);
	    
	    JPanel buttonPanel = new JPanel();
	    this.add(buttonPanel);
	    buttonPanel.setLayout(new FlowLayout());
	    
	    buttonPanel.add(cancelButton);
	    
	    this.cancelButton.setActionCommand(CANCEL_ACTION_COMMAND);
	    this.cancelButton.addActionListener(this);
	}

	/**
	 * Set default JFrame settings
	 */
	private void initialize() {
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setLayout(new GridLayout(3,1,0,0));//2 rows 1 column
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.toFront();
		this.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case CANCEL_ACTION_COMMAND:
				this.confirmCancel();
			break;
		}
	}
	private void confirmCancel() {
		int result = JOptionPane.showConfirmDialog(this,
			    "Are you sure you want to cancel creation?",
			    "CONFIRM",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.WARNING_MESSAGE);
		if(result == 0) {
			System.exit(0);
		}
	}

	public void display() {
		this.setVisible(true);
	}
	
	/**
	 * Notifies user that installation was successful and exits the application.
	 */
	public void completed() {
		JOptionPane.showMessageDialog(this,
			    INSTALLATION_SUCCESS_MESSAGE,
			    "Completed",
			    JOptionPane.PLAIN_MESSAGE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
