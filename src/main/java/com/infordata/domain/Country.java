package com.infordata.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="COUNTRIES")
public class Country {
	
	//Fields
	@Id
	@Column(name = "COUNTRY_ID")
	private String countryId;
	@Column(name = "COUNTRY_NAME")
	private String countryName;
	@Column(name = "REGION_ID")
	private Integer regionId;
	
	@OneToMany
	@JoinColumn(name="REGION_ID", referencedColumnName = "REGION_ID")
	private List<Region> region;
	public Country() {}
	//Constructor with fields
	public Country(String countryId, String countryName, Integer regionId, List<Region> region) {
		super();
		this.countryId = countryId;
		this.countryName = countryName;
		this.regionId = regionId;
		this.region = region;
	}

	//Getters and setters
	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public List<Region> getRegion() {
		return region;
	}

	public void setRegion(List<Region> region) {
		this.region = region;
	}
}
