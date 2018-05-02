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

 package com.trihydro.cvpt.model.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trihydro.cvpt.model.Vehicle;
import com.trihydro.cvpt.model.VehicleClass;
import com.trihydro.cvpt.model.VehicleEquipment;
import com.trihydro.cvpt.repository.VehicleRepository;
import com.trihydro.cvpt.repository.VehicleClassRepository;
import com.trihydro.cvpt.repository.VehicleEquipmentRepository;
import com.trihydro.cvpt.exceptions.RecordNotFoundException;

import java.util.List;


/**
 * A service to manage and describe vehicles, vehicle classes
 * and vehicle types.
 *
 *
 */
@Component
public class VehicleService
{

	private final VehicleRepository vehicleRepository;
	private final VehicleClassRepository vehicleClassRepository;
	private final VehicleEquipmentRepository vehicleEquipmentRepository;
	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	VehicleService(VehicleRepository vehicleRepository,
				   VehicleClassRepository vehicleClassRepository,
				   VehicleEquipmentRepository vehicleEquipmentRepository) 
	{
		this.vehicleRepository = vehicleRepository;
		this.vehicleClassRepository = vehicleClassRepository;
		this.vehicleEquipmentRepository = vehicleEquipmentRepository;
	}

	// get vehicle

	/**
	 * Returns the vehicle object identified by the given vehicle id.
	 *
	 * @param vehicleId - id to identify a specific vehicle record
	 *
	 * @return Vehicle associated with the given vehicleId
	 */
	public Vehicle getVehicle(Integer vehicleId) 
	{
		this.validateVehicle(vehicleId);
		return this.vehicleRepository.findOne(vehicleId);
	}

	/**
	 * Returns list of all vehicles in system
	 *
	 * @return a List of all vehicles
	 */
	public List<Vehicle> getAllVehicles() 
	{
		return this.vehicleRepository.findAll();
	}

	// get vehicle class

	/**
	 * Returns the VehicleClass object identified by the given 
	 * vehicle class id.
	 *
	 * @param vehicleClassId - id to identify a specific vehicle class
	 *
	 * @return a VehicleClass instance matching the given id
	 */
	public VehicleClass getVehicleClass(Integer vehicleClassId) 
	{
		validateVehicleClass(vehicleClassId);
		return vehicleClassRepository.findOne(vehicleClassId);
	}

	/**
	 * Returns list of all vehicle classes in system
	 *
	 * @return a List of all VehicleClasses
	 */
	public List<VehicleClass> getAllVehicleClasses() 
	{
		return this.vehicleClassRepository.findAll();
	}

	// get vehicle equipment

	/**
	 * Returns list of all vehicle equipment relations that are associated
	 * with the given vehicle
	 *
	 * @return a List of all VehicleEquipment relations for the given vehicle
	 */
	public List<VehicleEquipment> getAssociatedVehicleEquipment(Integer vehicleId) 
	{
		return this.vehicleEquipmentRepository.findByVehicleId(vehicleId);
	}

	/**
	 * Returns the vehicle equipment relation object identified by the given 
	 * vehicle equipment id.
	 *
	 * @param vehicleEquipmentId - identifies a specific vehicle equipment relation
	 *
	 * @return a VehicleEquipment instance matching the given id
	 */
	public VehicleEquipment getVehicleEquipment(Integer vehicleEquipmentId) 
	{
		this.validateVehicleEquipment(vehicleEquipmentId);
		return this.vehicleEquipmentRepository.findOne(vehicleEquipmentId);
	}

	// get available vehicles for participant

	/**
	 * Returns a list of all vehicles that are not currently assigned to 
	 * the specified participant. 
	 *
	 * @param  participantId - identifies a specific participant
	 *
	 * @return a List of vehicles not currently assigned to participant
	 */
	public List<Vehicle> getAvailableVehiclesForParticipant(Integer participantId) 
	{
		return this.vehicleRepository.getAvailableVehiclesForParticipant(participantId);
	}


	// add vehicle
	
	/**
	 * Adds the given vehicle to the repository or updates the
	 * vehicle if the vehicleId is specified.
	 *
	 * @param vehicle to add/update to the repository
	 */
	public void addVehicle(Vehicle vehicle) 
	{
		// confirm vehicle exists before updating
		if (vehicle.vehicleIdExists())
		{
			validateVehicle(vehicle.getVehicleId());
		}
		
		vehicleRepository.save(vehicle);
	}

	// add vehicle class

	/**
	 * Adds the given vehicleClass to the repository or updates the
	 * vehicleClass if the vehicleClassId is specified.
	 *
	 * @param vehicleClass to add/update to the repository
	 */
	public void addVehicleClass(VehicleClass vehicleClass) 
	{
		// confirm vehicle class exists before updating
		if (vehicleClass.vehicleClassIdExists())
		{
			validateVehicleClass(vehicleClass.getVehicleClassId());
		}
		
		vehicleClassRepository.save(vehicleClass);
	}


	// add vehicle equipment

	/**
	 * Adds the given vehicle equipment to the repository or 
	 * updates the vehicle equipment if the vehicleEquipmentId
	 * is specified.
	 *
	 * @param vehicleEquipment - vehicle equipment relation to add/update to the repository
	 */
	public void addVehicleEquipment(VehicleEquipment vehicleEquipment)
	{
		this.vehicleEquipmentRepository.save(vehicleEquipment);
	}

	// delete vehicle

	/**
	 * Deletes the given vehicle from the repository.
	 *
	 * @param vehicleId - identifies the vehicle to delete
	 */
	public void deleteVehicle(Integer vehicleId) 
	{
		this.validateVehicle(vehicleId);
		this.vehicleRepository.delete(vehicleId);
	}

	// delete vehicle class

	/**
	 * Deletes the given vehicle class from the repository.
	 *
	 * @param vehicleClassId - identifies the vehicle class to delete
	 */
	public void deleteVehicleClass(Integer vehicleClassId) 
	{
		this.validateVehicleClass(vehicleClassId);
		this.vehicleClassRepository.delete(vehicleClassId);
	}

	// delete vehicle equipment

	/**
	 * Deletes the given vehicle equipment relation from the repository.
	 *
	 * @param vehicleEquipmentId - identifies the vehicle equipment relation to delete
	 */
	public void deleteVehicleEquipment(Integer vehicleEquipmentId) 
	{
		this.validateVehicleEquipment(vehicleEquipmentId);
		this.vehicleEquipmentRepository.delete(vehicleEquipmentId);
	}


	/**
	 * Confirms that the requested vehicle is an existing one. 
	 * Throws a RecordNotFoundException if the given vehicle id
	 * does not match any vehicle records, but is silent if a 
	 * matching vehicle is found. 
	 *
	 * @param vehicleId - the vehicle id to validate
	 */
	private void validateVehicle(Integer vehicleId) 
	{
		this.vehicleRepository.findByVehicleId(vehicleId).orElseThrow(
			() -> new RecordNotFoundException("vehicle", 
											  "vehicle_id=" + vehicleId));
	}

	/**
	 * Confirms that the requested vehicle class is an existing one.
	 * Throws a RecordNotFoundException if the given vehicle class id
	 * does not match any vehicle class records, but is silent if a 
	 * matching vehicle class is found. 
	 *
	 * @param vehicleClassId - the vehicle class id to validate
	 */
	private void validateVehicleClass(Integer vehicleClassId) 
	{
		this.vehicleClassRepository.findByVehicleClassId(vehicleClassId).orElseThrow(
			() -> new RecordNotFoundException("vehicle_class", 
											  "vehicle_class_id=" + vehicleClassId));
	}

	/**
	 * Confirms that the requested vehicle equipment relation is an existing 
	 * one. Throws a RecordNotFoundException if the given vehicle equipment id
	 * does not match any vehicle equipment relation records, but is silent 
	 * if a matching vehicle equipment relation is found. 
	 *
	 * @param vehicleEquipmentId - the vehicle equipment id to validate
	 */
	private void validateVehicleEquipment(Integer vehicleEquipmentId)
	{
		this.vehicleEquipmentRepository.findByVehicleEquipmentId(vehicleEquipmentId).orElseThrow(
			() -> new RecordNotFoundException("vehicle_equipment",
											  "vehicle_equipment_id=" + vehicleEquipmentId));
	}

}