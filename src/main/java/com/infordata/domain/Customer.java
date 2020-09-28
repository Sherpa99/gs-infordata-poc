package com.infordata.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CUSTOMERS")
public class Customer {
	
	//Fields
	@Id
	@Column(name = "CUSTOMER_ID")
	private Integer customerId;
	@Column(name = "NAME")
	private String name;
	@Column(name = "ADDRESS")
	private String address;
	@Column(name = "WEBSITE")
	private String webSite;
	@Column(name = "CREDIT_LIMIT")
	private Long creditLimit;
	//Default constructor
	public Customer(){}
	//Constructor with fields
	public Customer(int customerId, String name, String address, String webSite, Long creditLimit) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.address = address;
		this.webSite = webSite;
		this.creditLimit = creditLimit;
	}

	//Getters and Setters
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public float getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Long creditLimit) {
		this.creditLimit = creditLimit;
	}
	
	
}
