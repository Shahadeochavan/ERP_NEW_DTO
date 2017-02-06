package com.nextech.erp.model;

import java.io.Serializable;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the productorder database table.
 * 
 */
@Entity
@NamedQuery(name="Productorder.findAll", query="SELECT p FROM Productorder p")
public class Productorder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	private long id;

	@Column(name="created_by")
	private int createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Temporal(TemporalType.DATE)
	private Date createDate;

	private String description;

	@Temporal(TemporalType.DATE)
	private Date expecteddeliveryDate;

	private boolean isactive;

	private int quantity;

	@Column(name="updated_by")
	private int updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	//bi-directional many-to-one association to Orderproductassociation
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "productorder", cascade = CascadeType.ALL)
	private List<Productorderassociation> orderproductassociations;

	//bi-directional many-to-one association to Status
	@ManyToOne
	@JoinColumn(name="statusid")
	private Status status;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="clientid")
	private Client client;

	public Productorder() {
	}
	public Productorder(int id) {
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

	public Date getExpecteddeliveryDate() {
		return this.expecteddeliveryDate;
	}

	public void setExpecteddeliveryDate(Date expecteddeliveryDate) {
		this.expecteddeliveryDate = expecteddeliveryDate;
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

	public List<Productorderassociation> getOrderproductassociations() {
		return this.orderproductassociations;
	}

	public void setOrderproductassociations(List<Productorderassociation> orderproductassociations) {
		this.orderproductassociations = orderproductassociations;
	}

	public Productorderassociation addOrderproductassociation(Productorderassociation orderproductassociation) {
		getOrderproductassociations().add(orderproductassociation);
		orderproductassociation.setProductorder(this);

		return orderproductassociation;
	}

	public Productorderassociation removeOrderproductassociation(Productorderassociation orderproductassociation) {
		getOrderproductassociations().remove(orderproductassociation);
		orderproductassociation.setProductorder(null);

		return orderproductassociation;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}