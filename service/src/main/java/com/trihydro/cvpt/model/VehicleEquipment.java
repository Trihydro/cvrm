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
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="vehicle_equipment")
public class VehicleEquipment
{

	private Integer    vehicleEquipmentId;
	private Integer	   vehicleId;
	private Equipment  equipment;

	/**
	 * Constructor - no arg for JPA
	 */
	public VehicleEquipment() {}

	// vehicleEquipmentId 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getVehicleEquipmentId() 
	{
		return this.vehicleEquipmentId;
	}

	public void setVehicleEquipmentId(Integer id)
	{
		this.vehicleEquipmentId = id;
	}

	// vehicleId
	public Integer getVehicleId() 
	{
		return this.vehicleId;
	}

	public void setVehicleId(Integer id)
	{
		this.vehicleId = id;
	}

	// equipment
	@OneToOne
	@JoinColumn(name="equipment_id")
	public Equipment getEquipment()
	{
		return this.equipment;
	}

	public void setEquipment(Equipment equipment)
	{
		this.equipment = equipment;
	}

}