package app;


import javax.swing.JOptionPane;

import app.config.ConfigManager;
import app.controller.MainController;

public class UsbApplication {

		public static void main(String args[]){
			System.out.println("The default path : "+ConfigManager.getString("defaultPath"));
			JOptionPane.showMessageDialog(null, "The current default path : "+ConfigManager.getString("defaultPath"));
			new UsbApplication();
	    }
		
		public UsbApplication() {
			this.createControllers();
		}
		
		private void createControllers() {
			MainController appController = new MainController();
			appController.startApplication();
		}
		
}
