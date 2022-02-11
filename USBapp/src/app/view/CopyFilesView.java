package app.view;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import app.controller.CopyFilesController;

/**
 * @author Sasha Bernans
 *
 */
public class CopyFilesView extends JFrame implements ActionListener{

	private static final String INSTALLATION_SUCCESS_MESSAGE = "Installation completed successfully.";
	private static final String SYSTEM_WARNING = "Do not turn off system this may take a few minutes";
	private static final String SPINNER_PATH = "optimized.gif";
	private static final String COPYING_FILES_MESSAGE = "Copying files to USB drive...";
	private static final int WINDOW_WIDTH = 350;
	private static final int WINDOW_HEIGHT = 150;
	private CopyFilesController controller;

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
		//Adds loading gif image to the frame.
		ImageIcon loading = new ImageIcon(getClass().getResource(SPINNER_PATH));
		JLabel gif = new JLabel(COPYING_FILES_MESSAGE, loading, JLabel.CENTER);
		
		//Adds warning to not turn system off.
		JLabel warning = new JLabel(SYSTEM_WARNING, JLabel.CENTER);
		warning.setFont(new Font("Serif",Font.PLAIN,14));
	    this.add(gif);
	    this.add(warning);
	}

	/**
	 * Set default JFrame settings
	 */
	private void initialize() {
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setLayout(new GridLayout(2,1,0,0));//2 rows 1 column
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.toFront();
		this.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
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
		this.dispose();
		System.exit(0);
	}
}
