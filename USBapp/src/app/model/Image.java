package app.model;

import java.util.ArrayList;

/**
 * This class represents an Image data model. It contains the paths to files and folders of the software and image
 * that the user specified in the MainView. It also contains the customer information filled out by the user.
 * @author Sasha Bernans
 *
 */
public class Image {
	
	private String TIBName;
	private ArrayList<String> softwareFolderNames;
	private String workOrder;
	private String salesOrder;
	private String customerName;
	private String description;
	private String defaultPath;
	
	public Image(String TIBName,
			ArrayList<String> softwareFolderNames,
			String workOrder,
			String salesOrder,
			String customerName,
			String description,
			String defaultPath){
		this.softwareFolderNames = softwareFolderNames;
		this.TIBName = TIBName;
		this.workOrder = workOrder;
		this.salesOrder = salesOrder;
		this.customerName = customerName;
		this.description = description;
		this.defaultPath = defaultPath;
	}

	public String getTIBName() {
		return TIBName;
	}

	public void setTIBName(String tIBPath) {
		TIBName = tIBPath;
	}

	public ArrayList<String> getSoftwareFolderNames() {
		return softwareFolderNames;
	}

	public void setSoftwareFolderNames(ArrayList<String> softwareFolderPaths) {
		this.softwareFolderNames = softwareFolderPaths;
	}

	public String getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}

	public String getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(String salesOrder) {
		this.salesOrder = salesOrder;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefaultPath() {
		return defaultPath;
	}

	public void setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
	}
}
