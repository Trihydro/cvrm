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

 package com.trihydro.cvpt.controller.dto;


import java.text.SimpleDateFormat;

import com.trihydro.cvpt.model.Equipment;



/**
 * Container object to transfer equipment data between 
 * web client and server.
 *
 */
public class EquipmentDTO 
{

	private Integer equipmentId;
	private String description;
	private String assetId;
	private Integer equipmentTypeId;
	private String equipmentType;
	private String serialNumber;
	private String modelNumber;
	private String wan;
	private Float latitude = null;
	private Float longitude = null;
	private Float elevation = null;
	private Float height = null;
	private String dateInstalled;
	private String notes;

	public EquipmentDTO() {}

	public EquipmentDTO(Equipment equipment) {
		this.equipmentId = equipment.getEquipmentId();
		this.description = equipment.getDescription();
		this.assetId = equipment.getAssetId();
		this.equipmentTypeId = equipment.getEquipmentType().getEquipmentTypeId();
		this.equipmentType = equipment.getEquipmentType().getEquipmentType();
		this.serialNumber = equipment.getSerialNumber();
		this.modelNumber = equipment.getModelNumber();
		this.wan = equipment.getWan();
		this.latitude = equipment.getLatitude();
		this.longitude = equipment.getLongitude();
		this.elevation = equipment.getElevation();
		this.height = equipment.getHeight();

		// only format the date if it is not null
		if (equipment.getDateInstalled() == null)
		{
			this.dateInstalled = null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			this.dateInstalled = df.format(equipment.getDateInstalled());
		}

		this.notes = equipment.getNotes();
		
	}

	/**
	 * Check if the equipmentId value exists. New equipment
	 * objects will not have an equipmentId, equipment to 
	 * update will have an equipmentId.
	 *
	 * @return True if an equipmentId value exists otherwise False
	 */
	public boolean equipmentIdExists()
	{
		return (equipmentId != null);
	}

	public Integer getEquipmentId() 
	{
		return equipmentId;
	}

	public void setEquipmentId(Integer id)
	{
		this.equipmentId = id;
	}

	public String getDescription() 
	{
		return description;
	}

	public String getAssetId()
	{
		return assetId;
	}

	public Integer getEquipmentTypeId()
	{
		return equipmentTypeId;
	}

	public String getEquipmentType()
	{
		return equipmentType;
	}

	public String getSerialNumber()
	{
		return serialNumber;
	}

	public String getModelNumber()
	{
		return modelNumber;
	}

	public String getWan()
	{
		return wan;
	}

	public Float getLatitude()
	{
		return latitude;
	}

	/**
	 * Check if the latitude value exists. For mobile equipment
	 * there are no values for latitude, longitude, elevation 
	 * and height.
	 *
	 * @return True if a latitude value exists otherwise False
	 */
	public boolean latitudeExists()
	{
		return (latitude != null);
	}

	public Float getLongitude()
	{
		return longitude;
	}

	public Float getElevation()
	{
		return elevation;
	}

	public Float getHeight()
	{
		return height;
	}

	public String getDateInstalled()
	{
		return dateInstalled;
	}

	public String getNotes()
	{
		return notes;
	}


}