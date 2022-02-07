package app.view;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import app.controller.ICopyFilesController;

/**
 * @author CASABER
 *
 */
public class CopyFilesView extends JFrame implements IView, ActionListener{

	private static final int WINDOW_WIDTH = 350;
	private static final int WINDOW_HEIGHT = 150;
	private ICopyFilesController controller;

	public CopyFilesView(ICopyFilesController filesController) {
		this.controller = filesController;
		
		this.initialize();
		this.setUpComponents();
		this.display();
		this.startCopier();
	}

	private void startCopier() {
		controller.copyFiles();
	}

	private void setUpComponents() {
		//adds loading gif image
		ImageIcon loading = new ImageIcon(getClass().getResource("optimized.gif"));
		JLabel gif = new JLabel("Copying files to USB drive...", loading, JLabel.CENTER);
		JLabel warning = new JLabel("Do not turn off system this may take a few minutes", JLabel.CENTER);
		warning.setFont(new Font("Serif",Font.PLAIN,14));
	    this.add(gif);
	    this.add(warning);
	}

	/**
	 * set default JFrame settings
	 */
	private void initialize() {
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setLayout(new GridLayout(2,1,0,0));//2 rows 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		this.toFront();
		this.requestFocus();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	@Override
	public void display() {
		this.setVisible(true);
	}
	
	/**
	 * Notifies user that installation was successful and exits the application.
	 */
	public void completed() {
		JOptionPane.showMessageDialog(this,
			    "Installation completed successfully.",
			    "Completed",
			    JOptionPane.PLAIN_MESSAGE);
		this.dispose();
		System.exit(0);
	}
}
