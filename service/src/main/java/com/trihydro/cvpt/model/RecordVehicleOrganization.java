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
import javax.persistence.Id;


@Entity
public class RecordVehicleOrganization
{
	private String  rowId;
	private String  organizationName;
	private String  vehicleClass;
	private Integer vehicleCount;

	/**
	 * Constructor - no arg for JPA
	 */
	RecordVehicleOrganization() {}

	// row id
	@Id
	public String getRowId() 
	{
		return rowId;
	}

	public void setRowId(String id)
	{
		this.rowId = id;
	}

	// organization name
	public String getOrganizationName() 
	{
		return this.organizationName;
	}

	public void setOrganizationName(String organizationName)
	{
		this.organizationName = organizationName;
	}

	// vehicle class
	public String getVehicleClass() 
	{
		return this.vehicleClass;
	}

	public void setVehicleClass(String vehicleClass)
	{
		this.vehicleClass = vehicleClass;
	}

	// vehicle count
	public Integer getVehicleCount() 
	{
		return this.vehicleCount;
	}

	public void setVehicleCount(Integer vehicleCount)
	{
		this.vehicleCount = vehicleCount;
	}
	
}