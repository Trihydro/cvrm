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


import com.trihydro.cvpt.model.EquipmentComponent;


/**
 * Container object to transfer equipment component data between 
 * web client and server.
 *
 */
public class EquipmentComponentDTO 
{

	private Long equipmentComponentId;
	private Integer equipmentComponentTypeId;
	private String equipmentComponentType;
	private Integer equipmentId;
	private String description;
	private String serialNumber;
	private String modelNumber;
	private String version;
	private Integer count;

	public EquipmentComponentDTO() {}

	public EquipmentComponentDTO(EquipmentComponent component) {
		this.equipmentComponentId = component.getEquipmentComponentId();
		this.equipmentComponentTypeId = component.getEquipmentComponentType().getEquipmentComponentTypeId();
		this.equipmentComponentType = component.getEquipmentComponentType().getEquipmentComponentType();
		this.equipmentId = component.getEquipmentId();
		this.description = component.getDescription();
		this.serialNumber = component.getSerialNumber();
		this.modelNumber = component.getModelNumber();
		this.version = component.getVersion();
		this.count = component.getCount();

	}

	/**
	 * Check if the equipmentComponentId value exists. New equipment
	 * component objects will not have an equipmentComponentId, 
	 * components to update will have an equipmentComponentId.
	 *
	 * @return True if an equipmentComponentId value exists otherwise False
	 */
	public boolean equipmentComponentIdExists()
	{
		return (equipmentComponentId != null);
	}

	public Long getEquipmentComponentId() 
	{
		return equipmentComponentId;
	}

	public void setEquipmentComponentId(Long id)
	{
		this.equipmentComponentId = id;
	}

	public Integer getEquipmentComponentTypeId()
	{
		return equipmentComponentTypeId;
	}

	public String getEquipmentComponentType()
	{
		return equipmentComponentType;
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

	public String getSerialNumber()
	{
		return serialNumber;
	}

	public String getModelNumber()
	{
		return modelNumber;
	}

	public String getVersion()
	{
		return version;
	}

	public Integer getCount()
	{
		return count;
	}

}