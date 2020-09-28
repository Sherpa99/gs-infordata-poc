package com.infordata.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="REGIONS")
public class Region {
	
	//Fields
	@Id
	@Column(name = "REGION_ID")
	private Integer regionId;
	@Column(name = "REGION_NAME")
	private String regionName;
	
	public Region() {}
	
	public Region(Integer regionId, String regionName) {
		super();
		this.regionId = regionId;
		this.regionName = regionName;
	}
	
	//Getters and setters
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

}
