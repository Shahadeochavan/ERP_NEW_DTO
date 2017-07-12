package com.nextech.erp.model;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Time;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the rawmaterialorderinvoice database table.
 *
 */
@Entity
@NamedQuery(name="Rawmaterialorderinvoice.findAll", query="SELECT r FROM Rawmaterialorderinvoice r")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rawmaterialorderinvoice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(name="created_by")
	private long createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Temporal(TemporalType.DATE)
	private Date createDate;

	@Size(min = 4, max = 255, message = "{description sholud be greater than 4 or less than 255 characters}")
	private String description;

	@Size(min = 2, max = 255, message = "{Driver name  sholud be greater than 2 or less than 255 characters}")
	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;
	
	private String licence_no;

	private Time intime;

	@Size(min = 2, max = 255, message = "{Invoice Number  sholud be greater than 2 or less than 255 characters or digits}")
	private String invoice_No;

	private boolean isactive;

	private Time outtime;

	@Column(name="`po.No`")
	private int po_No;

	@Column(name="updated_by")
	private long updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;


	@Size(min = 2, max = 255, message = "{Vehicle Number  sholud be greater than 2 or less than 255 characters or digits}")
	@Column(name="vehicle_no")
	private String vehicleNo;

	private String vendorname;

	//bi-directional many-to-one association to Qualitycheckrawmaterial

	@OneToMany(mappedBy="rawmaterialorderinvoice")
	private List<Qualitycheckrawmaterial> qualitycheckrawmaterials;

	//bi-directional many-to-one association to Rawmaterialorderhistory
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rawmaterialorderinvoice", cascade = CascadeType.ALL)
	private List<Rawmaterialorderhistory> rawmaterialorderhistories;

	//bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name="statusid")
	private Status status;

	//bi-directional many-to-one association to Rawmaterialorderinvoiceassociation
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "rawmaterialorderinvoice", cascade = CascadeType.ALL)
	private List<Rawmaterialorderinvoiceassociation> rawmaterialorderinvoiceassociations;

	//bi-directional many-to-one association to Rmorderinvoiceintakquantity
	@OneToMany(mappedBy="rawmaterialorderinvoice")
	//@JsonIgnore
	//@OneToMany(fetch = FetchType.LAZY, mappedBy = "rawmaterialorderinvoice", cascade = CascadeType.ALL)
	private List<Rmorderinvoiceintakquantity> rmorderinvoiceintakquantities;

	public Rawmaterialorderinvoice() {
	}
	public Rawmaterialorderinvoice(int id) {
		this.id=id;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return this.description;
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
	public String getLicence_no() {
		return licence_no;
	}
	public void setLicence_no(String licence_no) {
		this.licence_no = licence_no;
	}
	public Time getIntime() {
		return this.intime;
	}

	public void setIntime(Time intime) {
		this.intime = intime;
	}

	public String getInvoice_No() {
		return this.invoice_No;
	}

	public void setInvoice_No(String invoice_No) {
		this.invoice_No = invoice_No;
	}

	public boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public Time getOuttime() {
		return this.outtime;
	}

	public void setOuttime(Time outtime) {
		this.outtime = outtime;
	}

	public int getPo_No() {
		return this.po_No;
	}

	public void setPo_No(int po_No) {
		this.po_No = po_No;
	}

	public long getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getVehicleNo() {
		return this.vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getVendorname() {
		return this.vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public List<Qualitycheckrawmaterial> getQualitycheckrawmaterials() {
		return this.qualitycheckrawmaterials;
	}

	public void setQualitycheckrawmaterials(List<Qualitycheckrawmaterial> qualitycheckrawmaterials) {
		this.qualitycheckrawmaterials = qualitycheckrawmaterials;
	}

	public Qualitycheckrawmaterial addQualitycheckrawmaterial(Qualitycheckrawmaterial qualitycheckrawmaterial) {
		getQualitycheckrawmaterials().add(qualitycheckrawmaterial);
		qualitycheckrawmaterial.setRawmaterialorderinvoice(this);

		return qualitycheckrawmaterial;
	}

	public Qualitycheckrawmaterial removeQualitycheckrawmaterial(Qualitycheckrawmaterial qualitycheckrawmaterial) {
		getQualitycheckrawmaterials().remove(qualitycheckrawmaterial);
		qualitycheckrawmaterial.setRawmaterialorderinvoice(null);

		return qualitycheckrawmaterial;
	}

	public List<Rawmaterialorderhistory> getRawmaterialorderhistories() {
		return this.rawmaterialorderhistories;
	}

	public void setRawmaterialorderhistories(List<Rawmaterialorderhistory> rawmaterialorderhistories) {
		this.rawmaterialorderhistories = rawmaterialorderhistories;
	}

	public Rawmaterialorderhistory addRawmaterialorderhistory(Rawmaterialorderhistory rawmaterialorderhistory) {
		getRawmaterialorderhistories().add(rawmaterialorderhistory);
		rawmaterialorderhistory.setRawmaterialorderinvoice(this);

		return rawmaterialorderhistory;
	}

	public Rawmaterialorderhistory removeRawmaterialorderhistory(Rawmaterialorderhistory rawmaterialorderhistory) {
		getRawmaterialorderhistories().remove(rawmaterialorderhistory);
		rawmaterialorderhistory.setRawmaterialorderinvoice(null);

		return rawmaterialorderhistory;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Rawmaterialorderinvoiceassociation> getRawmaterialorderinvoiceassociations() {
		return this.rawmaterialorderinvoiceassociations;
	}

	public void setRawmaterialorderinvoiceassociations(List<Rawmaterialorderinvoiceassociation> rawmaterialorderinvoiceassociations) {
		this.rawmaterialorderinvoiceassociations = rawmaterialorderinvoiceassociations;
	}

	public Rawmaterialorderinvoiceassociation addRawmaterialorderinvoiceassociation(Rawmaterialorderinvoiceassociation rawmaterialorderinvoiceassociation) {
		getRawmaterialorderinvoiceassociations().add(rawmaterialorderinvoiceassociation);
		rawmaterialorderinvoiceassociation.setRawmaterialorderinvoice(this);

		return rawmaterialorderinvoiceassociation;
	}

	public Rawmaterialorderinvoiceassociation removeRawmaterialorderinvoiceassociation(Rawmaterialorderinvoiceassociation rawmaterialorderinvoiceassociation) {
		getRawmaterialorderinvoiceassociations().remove(rawmaterialorderinvoiceassociation);
		rawmaterialorderinvoiceassociation.setRawmaterialorderinvoice(null);

		return rawmaterialorderinvoiceassociation;
	}

	public List<Rmorderinvoiceintakquantity> getRmorderinvoiceintakquantities() {
		return this.rmorderinvoiceintakquantities;
	}

	public void setRmorderinvoiceintakquantities(List<Rmorderinvoiceintakquantity> rmorderinvoiceintakquantities) {
		this.rmorderinvoiceintakquantities = rmorderinvoiceintakquantities;
	}

	public Rmorderinvoiceintakquantity addRmorderinvoiceintakquantity(Rmorderinvoiceintakquantity rmorderinvoiceintakquantity) {
		getRmorderinvoiceintakquantities().add(rmorderinvoiceintakquantity);
		rmorderinvoiceintakquantity.setRawmaterialorderinvoice(this);

		return rmorderinvoiceintakquantity;
	}

	public Rmorderinvoiceintakquantity removeRmorderinvoiceintakquantity(Rmorderinvoiceintakquantity rmorderinvoiceintakquantity) {
		getRmorderinvoiceintakquantities().remove(rmorderinvoiceintakquantity);
		rmorderinvoiceintakquantity.setRawmaterialorderinvoice(null);

		return rmorderinvoiceintakquantity;
	}

}