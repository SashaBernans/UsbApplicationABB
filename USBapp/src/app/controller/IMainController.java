package app.controller;

import java.io.IOException;

import app.model.Image;

public interface IMainController {

	void startApplication();

	void goToSettings();

	void goToCopyFilesView(Image image, String usbPath);
}
