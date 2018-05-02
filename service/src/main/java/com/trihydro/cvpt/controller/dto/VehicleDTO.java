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


import com.trihydro.cvpt.model.Vehicle;


/**
 * Container object to transfer vehicle data between 
 * web client and server.
 *
 */
public class VehicleDTO 
{

	private Integer vehicleId;
	private String  id;
	private Integer vehicleClassId;
	private String  vehicleClass;
	private Integer organizationId;
	private String  organization;
	private String  vin;
	private String  make;
	private String  model;
	private String	notes;

	public VehicleDTO() {}

	public VehicleDTO(Vehicle vehicle) 
	{
		this.vehicleId = vehicle.getVehicleId();
		this.id = vehicle.getId();
		this.vehicleClassId = vehicle.getVehicleClass().getVehicleClassId();
		this.vehicleClass = vehicle.getVehicleClass().getVehicleClass();
		this.organizationId = vehicle.getOrganization().getOrganizationId();
		this.organization = vehicle.getOrganization().getName();
		this.vin = vehicle.getVin();
		this.make = vehicle.getMake();
		this.model = vehicle.getModel();
		this.notes = vehicle.getNotes();
	}

	/**
	 * Check if the vehicleId value exists. New vehicle
	 * objects will not have a vehicleId, vehicles to 
	 * update will have an vehicleId.
	 *
	 * @return True if an vehicleId value exists otherwise False
	 */
	public boolean vehicleIdExists(){return (vehicleId != null);}

	public Integer getVehicleId() {return vehicleId;}
	public void setVehicleId(Integer id) {this.vehicleId = id;}

	// id
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}

	// vehicleClassId
	public Integer getVehicleClassId() {return vehicleClassId;}

	// vehicleClass
	public String getVehicleClass() {return vehicleClass;}

	// organizationId
	public Integer getOrganizationId() {return organizationId;}

	// organization
	public String getOrganization() {return organization;}

	// vin
	public String getVin() {return vin;}
	public void setVin(String vin) {this.vin = vin;}

	// make
	public String getMake() {return make;}

	// model
	public String getModel() {return model;}

	// notes
	public String getNotes() {return notes;}

}