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


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="equipment_component")
public class EquipmentComponent
{

	
	private Long equipmentComponentId;
	private EquipmentComponentType equipmentComponentType;
	private Integer equipmentId;
	private String description;
	private String serialNumber;
	private String modelNumber;
	private String version;
	private Integer count;

	/**
	 * Constructor - no arg for JPA
	 */
	public EquipmentComponent() {}

	// equipmentComponentId 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getEquipmentComponentId() 
	{
		return this.equipmentComponentId;
	}

	public void setEquipmentComponentId(Long id)
	{
		this.equipmentComponentId = id;
	}

	// equipmentComponentType
	@ManyToOne
	@JoinColumn(name="equipment_component_type_id")
	public EquipmentComponentType getEquipmentComponentType() 
	{
		return this.equipmentComponentType;
	}

	public void setEquipmentComponentType(EquipmentComponentType componentType)
	{
		this.equipmentComponentType = componentType;
	}

	// equipmentId
	public Integer getEquipmentId()
	{
		return this.equipmentId;
	}

	public void setEquipmentId(Integer id)
	{
		this.equipmentId = id;
	}

	// description
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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

	// version
	public String getVersion() 
	{
		return this.version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	// count
	public Integer getCount() 
	{
		return this.count;
	}

	public void setCount(Integer count)
	{
		this.count = count;
	}

}