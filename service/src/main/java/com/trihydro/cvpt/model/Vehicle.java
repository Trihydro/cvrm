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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="vehicle")
public class Vehicle 
{

	
	private Integer vehicleId;
	private String id;
	private VehicleClass vehicleClass;
	private Organization organization;
	private String vin;
	private String make;
	private String model;
	private String notes;


	/**
	 * Constructor - no arg for JPA
	 */
	public Vehicle() {}

	// vehicleId 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getVehicleId() {
		return this.vehicleId;
	}

	public void setVehicleId(Integer id)
	{
		this.vehicleId = id;
	}

	/**
	 * Checks if an vehicle id is defined
	 *
	 * @return True if id is defined otherwise False
	 */
	public boolean vehicleIdExists()
	{
		return (vehicleId != null);
	}

	// id
	public String getId() {
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	// vehicleClass
	@ManyToOne
	@JoinColumn(name="vehicle_class_id")
	public VehicleClass getVehicleClass() {
		return this.vehicleClass;
	}

	public void setVehicleClass(VehicleClass vehicleClass)
	{
		this.vehicleClass = vehicleClass;
	}

	// organization
	@ManyToOne
	@JoinColumn(name="organization_id")
	public Organization getOrganization() {
		return this.organization;
	}

	public void setOrganization(Organization organization)
	{
		this.organization = organization;
	}

	// vin
	public String getVin() 
	{
		return this.vin;
	}

	public void setVin(String vin)
	{
		this.vin = vin;
	}

	// make
	public String getMake() 
	{
		return this.make;
	}

	public void setMake(String make)
	{
		this.make = make;
	}

	// model
	public String getModel() 
	{
		return this.model;
	}

	public void setModel(String model)
	{
		this.model = model;
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