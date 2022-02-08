package app;

import app.controller.IMainController;
import app.controller.MainController;

public class UsbApplication {

		public static void main(String args[]){
			new UsbApplication();
	    }
		
		public UsbApplication() {
			this.createControllers();
		}
		
		private void createControllers() {
			IMainController appController = new MainController();
			appController.startApplication();
		}
}
