package com.nextech.erp.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the reportinputparameter database table.
 * 
 */
@Entity
@NamedQuery(name="Reportinputparameter.findAll", query="SELECT r FROM Reportinputparameter r")
public class Reportinputparameter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="created_by")
	private int createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Column(name="display_name")
	private String displayName;

	@Column(name="input_typer")
	private String inputTyper;

	private byte isactive;

	private String isQueryParameter;

	private String query;

	@Column(name="updated_by")
	private int updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	//bi-directional many-to-one association to Reportinputassociation
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reportinputparameter", cascade = CascadeType.ALL)
	private List<Reportinputassociation> reportinputassociations;

	public Reportinputparameter() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getInputTyper() {
		return this.inputTyper;
	}

	public void setInputTyper(String inputTyper) {
		this.inputTyper = inputTyper;
	}

	public byte getIsactive() {
		return this.isactive;
	}

	public void setIsactive(byte isactive) {
		this.isactive = isactive;
	}

	public String getIsQueryParameter() {
		return this.isQueryParameter;
	}

	public void setIsQueryParameter(String isQueryParameter) {
		this.isQueryParameter = isQueryParameter;
	}

	public String getQuery() {
		return this.query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<Reportinputassociation> getReportinputassociations() {
		return this.reportinputassociations;
	}

	public void setReportinputassociations(List<Reportinputassociation> reportinputassociations) {
		this.reportinputassociations = reportinputassociations;
	}

	public Reportinputassociation addReportinputassociation(Reportinputassociation reportinputassociation) {
		getReportinputassociations().add(reportinputassociation);
		reportinputassociation.setReportinputparameter(this);

		return reportinputassociation;
	}

	public Reportinputassociation removeReportinputassociation(Reportinputassociation reportinputassociation) {
		getReportinputassociations().remove(reportinputassociation);
		reportinputassociation.setReportinputparameter(null);

		return reportinputassociation;
	}

}