/********************************************************************
 *  Copyright 2016 Trihydro 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ********************************************************************/

 package com.trihydro.cvpt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Table;

import java.util.Date;


@Entity
@Table(name="equipment")
public class Equipment 
{

	
	private Integer equipmentId;
	private String description;
	private String assetId;
	private EquipmentType equipmentType;
	private String serialNumber;
	private String modelNumber;
	private String wan;
	private Float latitude;
	private Float longitude;
	private Float elevation;
	private Float height;
	private Date dateInstalled;
	private String notes;

	/**
	 * Constructor - no arg for JPA
	 */
	public Equipment() {}

	// equipmentId 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getEquipmentId() {
		return this.equipmentId;
	}

	public void setEquipmentId(Integer id)
	{
		this.equipmentId = id;
	}

	/**
	 * Checks if an equipment id is defined
	 *
	 * @return True if id is defined otherwise False
	 */
	public boolean equipmentIdExists()
	{
		return (equipmentId != null);
	}

	// description
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	// assetId
	public String getAssetId() 
	{
		return this.assetId;
	}

	public void setAssetId(String assetId)
	{
		this.assetId = assetId;
	}

	// equipmentType
	@ManyToOne
	@JoinColumn(name="equipment_type_id")
	public EquipmentType getEquipmentType() {
		return this.equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType)
	{
		this.equipmentType = equipmentType;
	}

	// serialNumber
	public String getSerialNumber() 
	{
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber = serialNumber;
	}

	// modelNumber
	public String getModelNumber() 
	{
		return this.modelNumber;
	}

	public void setModelNumber(String modelNumber)
	{
		this.modelNumber = modelNumber;
	}

	// wan
	public String getWan() 
	{
		return this.wan;
	}

	public void setWan(String wan)
	{
		this.wan = wan;
	}

	// latitude
	public Float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(Float latitude)
	{
		this.latitude = latitude;
	}

	// longitude
	public Float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(Float longitude)
	{
		this.longitude = longitude;
	}

	// elevation
	public Float getElevation() {
		return this.elevation;
	}

	public void setElevation(Float elevation)
	{
		this.elevation = elevation;
	}

	// height
	public Float getHeight() 
	{
		return this.height;
	}

	public void setHeight(Float height)
	{
		this.height = height;
	}

	// dateInstalled
	@Temporal(TemporalType.DATE)
	public Date getDateInstalled()
	{
		return this.dateInstalled;
	}

	public void setDateInstalled(Date dateInstalled)
	{
		this.dateInstalled = dateInstalled;
	}

	// notes
	public String getNotes()
	{
		return this.notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}
}