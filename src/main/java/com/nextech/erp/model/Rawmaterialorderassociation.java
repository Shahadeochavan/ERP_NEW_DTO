package com.nextech.erp.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.sql.Timestamp;


/**
 * The persistent class for the rawmaterialorderassociation database table.
 * 
 */
@Entity
@NamedQuery(name="Rawmaterialorderassociation.findAll", query="SELECT r FROM Rawmaterialorderassociation r")
public class Rawmaterialorderassociation implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;

	@Column(name="created_by")
	private int createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	private boolean isactive;

	private int quantity;
	
	private int remainingQuantity;

	@Column(name="updated_by")
	private int updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	//bi-directional many-to-one association to Rawmaterial
	@ManyToOne
	private Rawmaterial rawmaterial;

	//bi-directional many-to-one association to Rawmaterialorder
	@ManyToOne
	@JoinColumn(name="order_id")
	 @JsonBackReference
	private Rawmaterialorder rawmaterialorder;

	public Rawmaterialorderassociation() {
	}
	public Rawmaterialorderassociation(int id) {
		this.id=id;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
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

	public boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public Rawmaterial getRawmaterial() {
		return this.rawmaterial;
	}

	public void setRawmaterial(Rawmaterial rawmaterial) {
		this.rawmaterial = rawmaterial;
	}

	public Rawmaterialorder getRawmaterialorder() {
		return this.rawmaterialorder;
	}

	public void setRawmaterialorder(Rawmaterialorder rawmaterialorder) {
		this.rawmaterialorder = rawmaterialorder;
	}
	public int getRemainingQuantity() {
		return remainingQuantity;
	}
	public void setRemainingQuantity(int remainingQuantity) {
		this.remainingQuantity = remainingQuantity;
	}

}