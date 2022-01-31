package app.image;

import java.util.ArrayList;

public class Image {
	
	private String TIBName;
	private ArrayList<String> softwareFolderNames;
	private String workOrder;
	private String salesOrder;
	private String customerName;
	private String description;
	
	public Image(String TIBName,
			ArrayList<String> softwareFolderNames,
			String workOrder,
			String salesOrder,
			String customerName,
			String description){
		this.setSoftwareFolderNames(softwareFolderNames);
		this.setTIBName(TIBName);
		this.setWorkOrder(workOrder);
		this.setSalesOrder(salesOrder);
		this.setCustomerName(customerName);
		this.setDescription(description);
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
}
