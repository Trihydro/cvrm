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


import com.trihydro.cvpt.model.VehicleEquipment;

/**
 * Container object to transfer vehicle equipment data between 
 * web client and server.
 *
 */
public class VehicleEquipmentDTO 
{

	private Integer vehicleEquipmentId;
	private Integer vehicleId;
	private Integer equipmentId;
	private String  assetId;   //from equipment

	public VehicleEquipmentDTO() {}

	public VehicleEquipmentDTO(VehicleEquipment vehicleEquipment) 
	{
		this.vehicleEquipmentId = vehicleEquipment.getVehicleEquipmentId();
		this.vehicleId = vehicleEquipment.getVehicleId();
		this.equipmentId = vehicleEquipment.getEquipment().getEquipmentId();
		this.assetId = vehicleEquipment.getEquipment().getAssetId();
	}

	/**
	 * Check if the vehicleEquipmentId value exists. New vehicle 
	 * equipment relation objects will not have a vehicleEquipmentId, 
	 * vehicle equipment relations to update will have a vehicleEquipmentId.
	 *
	 * @return True if a vehicleEquipmentId value exists otherwise False
	 */
	public boolean vehicleEquipmentIdExists()
	{
		return (vehicleEquipmentId != null);
	}

	public Integer getVehicleEquipmentId()
	{
		return vehicleEquipmentId;
	}

	public void setVehicleEquipmentId(Integer id)
	{
		this.vehicleEquipmentId = id;
	}

	public Integer getVehicleId()
	{
		return vehicleId;
	}

	public void setVehicleId(Integer id)
	{
		this.vehicleId = id;
	}

	public Integer getEquipmentId()
	{
		return equipmentId;
	}

	public String getAssetId()
	{
		return assetId;
	}

}