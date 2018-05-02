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
@Table(name = "equipment_component_type")
public class EquipmentComponentType
{
	
	private Integer equipmentComponentTypeId;
	public String equipmentComponentType;

	/**
	 * Constructor - default for application context
	 * create new equipment component type instance
	 */
	public EquipmentComponentType(String equipmentComponentType) 
	{
		this.equipmentComponentType = equipmentComponentType;
	}

	/**
	 * Constructor - default for application context
	 * update existing equipment component type instance
	 */
	public EquipmentComponentType(Integer equipmentComponentTypeId,
		                		  String equipmentComponentType)
	{
		this.equipmentComponentTypeId = equipmentComponentTypeId;
		this.equipmentComponentType = equipmentComponentType;
	}

	/**
	 * Constructor - no arg for JPA
	 */
	EquipmentComponentType() {}

	// equipment component type id
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getEquipmentComponentTypeId() 
	{
		return equipmentComponentTypeId;
	}

	public void setEquipmentComponentTypeId(Integer id)
	{
		this.equipmentComponentTypeId = id;
	}

	// equipment component type
	public String getEquipmentComponentType() 
	{
		return this.equipmentComponentType;
	}

	public void setEquipmentComponentType(String componentType)
	{
		this.equipmentComponentType = componentType;
	}
	
}