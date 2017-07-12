package com.nextech.erp.newDTO;




import java.sql.Time;
import java.util.Date;

public class RMOrderInvoiceDTO {

	
	private long id;
	
	private Date createDate;

	private String description;

	private String firstName;

	private String lastName;

	private Time intime;

	private String invoice_No;
	
	private String licence_no;

	private Time outtime;

	private int po_No;

	private String vehicleNo;

	private String vendorname;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Time getIntime() {
		return intime;
	}

	public void setIntime(Time intime) {
		this.intime = intime;
	}

	public String getInvoice_No() {
		return invoice_No;
	}

	public void setInvoice_No(String invoice_No) {
		this.invoice_No = invoice_No;
	}

	public String getLicence_no() {
		return licence_no;
	}

	public void setLicence_no(String licence_no) {
		this.licence_no = licence_no;
	}

	public Time getOuttime() {
		return outtime;
	}

	public void setOuttime(Time outtime) {
		this.outtime = outtime;
	}

	public int getPo_No() {
		return po_No;
	}

	public void setPo_No(int po_No) {
		this.po_No = po_No;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}
	
	
}
