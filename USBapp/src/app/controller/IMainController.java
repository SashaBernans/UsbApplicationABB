package app.controller;

import java.io.IOException;

import app.image.Image;

public interface IMainController {

	void startApplication();

	void goToSettings();

	void createImage(Image image, String usbPath);

}
