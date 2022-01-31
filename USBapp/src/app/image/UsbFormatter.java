package app.image;

import java.io.IOException;

public class UsbFormatter {
	public UsbFormatter() {
		
	}
	
	public void formatToNTFS(String drive) {
		try {
			Runtime.getRuntime().exec("FORMAT "+drive.replace("\\","")+" /FS:NTFS");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
