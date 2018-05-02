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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Table(name = "equipment_type")
public class EquipmentType
{
	
	private Integer equipmentTypeId;
	private String equipmentType;

	/**
	 * Constructor - default for application context
	 * create new equipment type instance
	 */
	public EquipmentType(String equipmentType) 
	{
		this.equipmentType = equipmentType;
	}

	/**
	 * Constructor - default for application context
	 * update existing equipment type instance
	 */
	public EquipmentType(Integer equipmentTypeId,
		                String equipmentType)
	{
		this.equipmentTypeId = equipmentTypeId;
		this.equipmentType = equipmentType;
	}

	/**
	 * Constructor - no arg for JPA
	 */
	EquipmentType() {}

	// equipment type id
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getEquipmentTypeId() 
	{
		return equipmentTypeId;
	}

	public void setEquipmentTypeId(Integer id)
	{
		this.equipmentTypeId = id;
	}

	/**
	 * Checks if an equipment type id is defined
	 *
	 * @return True if id is defined otherwise False
	 */
	public boolean equipmentTypeIdExists()
	{
		return (equipmentTypeId != null);
	}

	// equipment type
	public String getEquipmentType() 
	{
		return this.equipmentType;
	}

	public void setEquipmentType(String equipmentType)
	{
		this.equipmentType = equipmentType;
	}
	
}