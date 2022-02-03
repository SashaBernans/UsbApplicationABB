package app.image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UsbFormatter {
	public UsbFormatter() {
		
	}
	
	public void formatToNTFS(String drive, String workOrder) {
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
