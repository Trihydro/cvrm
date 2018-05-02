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


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "vehicle_class")
public class VehicleClass
{
	
	private Integer vehicleClassId;
	private Integer vehicleClassNumber;
	private String  vehicleClass;
	private String  description;

	/**
	 * Constructor - default for application context
	 * create new vehicle class instance
	 */
	public VehicleClass(Integer classNumber,
						String vehicleClass) 
	{
		this.vehicleClassNumber = classNumber;
		this.vehicleClass = vehicleClass;
	}

	/**
	 * Constructor - default for application context
	 * update existing vehicle class instance
	 */
	public VehicleClass(Integer vehicleClassId,
						Integer vehicleClassNumber,
		                String vehicleClass)
	{
		this.vehicleClassId = vehicleClassId;
		this.vehicleClassNumber = vehicleClassNumber;
		this.vehicleClass = vehicleClass;
	}

	/**
	 * Constructor - no arg for JPA
	 */
	VehicleClass() {}

	// vehicle class id
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getVehicleClassId() 
	{
		return vehicleClassId;
	}

	public void setVehicleClassId(Integer id)
	{
		this.vehicleClassId = id;
	}

	/**
	 * Checks if an vehicle class id is defined
	 *
	 * @return True if id is defined otherwise False
	 */
	public boolean vehicleClassIdExists()
	{
		return (vehicleClassId != null);
	}

	// vehicle class number
	public Integer getVehicleClassNumber() 
	{
		return this.vehicleClassNumber;
	}

	public void setVehicleClassNumber(Integer classNumber)
	{
		this.vehicleClassNumber = classNumber;
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

	// description
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}
	
}