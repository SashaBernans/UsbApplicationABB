package app.view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import app.controller.ICopyFilesController;

public class CopyFilesView extends JFrame implements IView, ActionListener{

	private static final int WINDOW_WIDTH = 350;
	private static final int WINDOW_HEIGHT = 200;
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
		ImageIcon loading = new ImageIcon(getClass().getResource("spinning-loading.gif"));
	    this.add(new JLabel("loading... ", loading, JLabel.CENTER));
	}

	private void initialize() {
		this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

	@Override
	public void display() {
		this.setVisible(true);
	}

}
