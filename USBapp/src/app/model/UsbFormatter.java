package app.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class can be used to format the usbDrive if needed before copying files to it.
 * @author CASABER
 *
 */
public class UsbFormatter{
	private String drive;
	private String workOrder;
	
	/**
	 * @param drive : to be formatted
	 * @param workOrder : new label for the drive
	 */
	public UsbFormatter(String drive, String workOrder) {
		this.drive = drive;
		this.workOrder = workOrder;
	}
	
	/**
	 * Formats the USB drive with NTFS and changes name to workOrder using cmd.exe
	 */
	public void formatToNTFS() {
		String letter = drive.replace("\\","");
		try {
			//execute format process in command promtp
			Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c","format /y "+letter+" /v:"+workOrder+" /FS:NTFS /Q"}); 
		
			// printing the results
			  process.getOutputStream().close();
			  String line;
			  System.out.println("Standard Output:");
			  BufferedReader stdout = new BufferedReader(new InputStreamReader(
			    process.getInputStream()));
			  while ((line = stdout.readLine()) != null) {
			   System.out.println(line);
			  }
			  
			//Print out errors
			  stdout.close();
			  System.out.println("Standard Error:");
			  BufferedReader stderr = new BufferedReader(new InputStreamReader(
			    process.getErrorStream()));
			  while ((line = stderr.readLine()) != null) {
			   System.out.println(line);
			  }
			  stderr.close();
			  System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
