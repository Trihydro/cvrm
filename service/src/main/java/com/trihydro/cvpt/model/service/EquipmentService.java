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

import com.trihydro.cvpt.model.Equipment;
import com.trihydro.cvpt.model.EquipmentType;
import com.trihydro.cvpt.model.EquipmentComponent;
import com.trihydro.cvpt.model.EquipmentComponentType;
import com.trihydro.cvpt.repository.EquipmentComponentRepository;
import com.trihydro.cvpt.repository.EquipmentComponentTypeRepository;
import com.trihydro.cvpt.repository.EquipmentRepository;
import com.trihydro.cvpt.repository.EquipmentTypeRepository;
import com.trihydro.cvpt.exceptions.RecordNotFoundException;

import java.util.List;


/**
 * A service to manage and describe equipment and equipment types.
 *
 *
 */
@Component
public class EquipmentService
{

	private final EquipmentTypeRepository equipmentTypeRepository;
	private final EquipmentRepository equipmentRepository;
	private final EquipmentComponentRepository equipmentComponentRepository;
	private final EquipmentComponentTypeRepository equipmentComponentTypeRepository;
	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	EquipmentService(EquipmentRepository equipmentRepository,
					 EquipmentTypeRepository equipmentTypeRepository,
					 EquipmentComponentRepository equipmentComponentRepository,
					 EquipmentComponentTypeRepository equipmentComponentTypeRepository) 
	{
		this.equipmentTypeRepository = equipmentTypeRepository;
		this.equipmentRepository = equipmentRepository;
		this.equipmentComponentRepository = equipmentComponentRepository;
		this.equipmentComponentTypeRepository = equipmentComponentTypeRepository;
	}


	/**
	 * Returns the equipment object identified by the given equipment id.
	 *
	 * @param equipmentId  id to identify a specific equipment record
	 *
	 * @return Equipment equipment associated with the given equipmentId
	 */
	public Equipment getEquipment(Integer equipmentId) 
	{
		this.validateEquipment(equipmentId);
		return this.equipmentRepository.findOne(equipmentId);
	}

	/**
	 * Returns list of all equipment in system
	 *
	 * @return a List of all equipment
	 */
	public List<Equipment> getAllEquipment() 
	{
		return this.equipmentRepository.findAll();
	}

	/**
	 * Returns a list of all vehicle equipment (non RSU types) that are 
	 * not currently assigned to a vehicle. 
	 *
	 * @return a List of mobile equipment not currently assigned to any vehicle
	 */
	public List<Equipment> getAvailableEquipmentForVehicle() 
	{
		return this.equipmentRepository.getAvailableVehicleEquipment();
	}

	/**
	 * Returns list of all equipment types in system
	 *
	 * @return a List of all equipment types
	 */
	public List<EquipmentType> getAllEquipmentTypes() 
	{
		return this.equipmentTypeRepository.findAll();
	}

	/**
	 * Returns list of all components related to the
	 * given equipment
	 *
	 * @return a List of all components for the given equipment
	 */
	public List<EquipmentComponent> getEquipmentComponents(Integer equipmentId) 
	{
		return this.equipmentComponentRepository.findByEquipmentId(equipmentId);
	}

	/**
	 * Returns the equipment component object identified by the given 
	 * equipment component id.
	 *
	 * @param equipmentComponentId - id to identify a specific equipment component
	 *
	 * @return an EquipmentComponent instance matching the given id
	 */
	public EquipmentComponent getEquipmentComponent(Long componentId) 
	{
		this.validateEquipmentComponent(componentId);
		return this.equipmentComponentRepository.findOne(componentId);
	}

	/**
	 * Returns the equipment component type object identified by the given 
	 * equipment component type id.
	 *
	 * @param equipmentComponentTypeId - id identifies a specific equipment component type
	 *
	 * @return an EquipmentComponentType instance matching the given id
	 */
	public EquipmentComponentType getEquipmentComponentType(Integer componentTypeId) 
	{
		this.validateEquipmentComponentType(componentTypeId);
		return this.equipmentComponentTypeRepository.findOne(componentTypeId);
	}

	/**
	 * Returns list of all equipment component types in system
	 *
	 * @return a List of all equipment component types
	 */
	public List<EquipmentComponentType> getAllEquipmentComponentTypes() 
	{
		return this.equipmentComponentTypeRepository.findAll();
	}


	/**
	 * Returns the equipment type object identified by the given 
	 * equipment type id.
	 *
	 * @param equipmentTypeId - id to identify a specific equipment type
	 *
	 * @return an EquipmentType instance matching the given id
	 */
	public EquipmentType getEquipmentType(Integer equipmentTypeId) 
	{
		validateEquipmentType(equipmentTypeId);
		return equipmentTypeRepository.findOne(equipmentTypeId);
	}

	/**
	 * Adds the given equipment to the repository or updates the
	 * equipment if the equipmentId is specified.
	 *
	 * @param equipment to add/update to the repository
	 */
	public void addEquipment(Equipment equipment) 
	{
		// confirm equipment exists before updating
		if (equipment.equipmentIdExists())
		{
			validateEquipment(equipment.getEquipmentId());
		}
		
		equipmentRepository.save(equipment);
	}

	/**
	 * Adds the given equipment type to the repository or updates
	 * the equipment type if the equipmentTypeId is specified.
	 *
	 * @param EquipmentType to add/update to the repository
	 */
	public void addEquipmentType(EquipmentType equipmentType) 
	{
		// confirm equipment type exists before updating
		if (equipmentType.equipmentTypeIdExists())
		{
			validateEquipmentType(equipmentType.getEquipmentTypeId());
		}

		this.equipmentTypeRepository.save(equipmentType);
	}

	/**
	 * Adds the given equipment component to the repository or 
	 * updates the equipment component if the equipmentComponentId
	 * is specified.
	 *
	 * @param EquipmentCompoent to add/update to the repository
	 */
	public void addEquipmentComponent(EquipmentComponent equipmentComponent)
	{
		this.equipmentComponentRepository.save(equipmentComponent);
	}

	/**
	 * Adds the given equipment component type to the repository 
	 * or updates the equipment type if the equipmentComponentTypeId 
	 * is specified.
	 *
	 * @param EquipmentComponentType to add/update to the repository
	 */
	public void addEquipmentComponentType(EquipmentComponentType componentType) 
	{
		this.equipmentComponentTypeRepository.save(componentType);
	}

	/**
	 * Deletes the given equipment from the repository.
	 *
	 * @param equipmentId - identifies the equipment to delete
	 */
	public void deleteEquipment(Integer equipmentId) 
	{
		this.validateEquipment(equipmentId);
		this.equipmentRepository.delete(equipmentId);
	}

	/**
	 * Deletes the given equipment type from the repository.
	 *
	 * @param equipmentTypeId - identifies the equipment type to delete
	 */
	public void deleteEquipmentType(Integer equipmentTypeId) 
	{
		this.validateEquipmentType(equipmentTypeId);
		this.equipmentTypeRepository.delete(equipmentTypeId);
	}

	/**
	 * Deletes the given equipment component from the repository.
	 *
	 * @param componentId - identifies the equipment component to delete
	 */
	public void deleteEquipmentComponent(Long componentId) 
	{
		this.validateEquipmentComponent(componentId);
		this.equipmentComponentRepository.delete(componentId);
	}


	/**
	 * Deletes the given equipment component type from the repository.
	 *
	 * @param equipmentComponentTypeId - identifies the component type to delete
	 */
	public void deleteEquipmentComponentType(Integer componentTypeId) 
	{
		this.validateEquipmentComponentType(componentTypeId);
		this.equipmentComponentTypeRepository.delete(componentTypeId);
	}


	/**
	 * Confirms that the requested equipment is an existing one. 
	 * Throws a RecordNotFoundException if the given equipment id
	 * does not match any equipment records, but is silent if a 
	 * matching equipment is found. 
	 *
	 * @param equipmentId - the equipment id to validate
	 */
	private void validateEquipment(Integer equipmentId) 
	{
		this.equipmentRepository.findByEquipmentId(equipmentId).orElseThrow(
			() -> new RecordNotFoundException("equipment", 
											  "equipment_id=" + equipmentId));
	}

	/**
	 * Confirms that the requested equipment component is an existing one.
	 * Throws a RecordNotFoundException if the given equipment component id
	 * does not match any equipment component records, but is silent if a 
	 * matching equipment component is found. 
	 *
	 * @param equipmentComponentId - the equipment component id to validate
	 */
	private void validateEquipmentComponent(Long componentId)
	{
		this.equipmentComponentRepository.findByEquipmentComponentId(componentId).orElseThrow(
			() -> new RecordNotFoundException("equipment_component",
											  "equipent_component_id=" + componentId));
	}

	/**
	 * Confirms that the requested equipment type is an existing one.
	 * Throws a RecordNotFoundException if the given equipment type id
	 * does not match any equipment type records, but is silent if a 
	 * matching equipment type is found. 
	 *
	 * @param equipmentTypeId - the equipment type id to validate
	 */
	private void validateEquipmentType(Integer equipmentTypeId) 
	{
		this.equipmentTypeRepository.findByEquipmentTypeId(equipmentTypeId).orElseThrow(
			() -> new RecordNotFoundException("equipment_type", 
											  "equipment_type_id=" + equipmentTypeId));
	}

	/**
	 * Confirms that the requested equipment component type is an existing one.
	 * Throws a RecordNotFoundException if the given equipment component type id
	 * does not match any equipment component type records, but is silent if a 
	 * matching equipment component type is found. 
	 *
	 * @param equipmentComponentTypeId - the equipment component type id to validate
	 */
	private void validateEquipmentComponentType(Integer componentTypeId) 
	{
		this.equipmentComponentTypeRepository.findByEquipmentComponentTypeId(componentTypeId).orElseThrow(
			() -> new RecordNotFoundException("equipment_component_type", 
											  "equipment_component_type_id=" + componentTypeId));
	}

}