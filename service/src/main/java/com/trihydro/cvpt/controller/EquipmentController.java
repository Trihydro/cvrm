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

 package com.trihydro.cvpt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
//import java.util.Iterator;
import java.util.stream.Collectors;

import com.trihydro.cvpt.model.*;
import com.trihydro.cvpt.model.service.EquipmentService;
import com.trihydro.cvpt.controller.dto.EquipmentDTO;
import com.trihydro.cvpt.controller.dto.EquipmentComponentDTO;
import com.trihydro.cvpt.exceptions.RecordNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/cvpt")
class EquipmentController 
{
	private final EquipmentService equipmentService;
	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	EquipmentController(EquipmentService equipmentService) 
	{
		this.equipmentService = equipmentService;
	}


	/*
	 * Equipment Types requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/equipmenttypes")
	Collection<EquipmentType> readEquipmentType(ServletWebRequest request) 
	{
		return this.equipmentService.getAllEquipmentTypes();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/equipmenttypes/{equipmentTypeId}")
	EquipmentType readEquipmentType(@PathVariable Integer equipmentTypeId, 
												  ServletWebRequest request) 
	{
		return this.equipmentService.getEquipmentType(equipmentTypeId);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/equipmenttypes/{equipmentTypeId}")
	EquipmentType saveEquipmentType(@PathVariable Integer equipmentTypeId, 
									@RequestBody EquipmentType input,
									             ServletWebRequest request) 
	{
		EquipmentType equipmentType = new EquipmentType(equipmentTypeId, input.getEquipmentType());
		equipmentService.addEquipmentType(equipmentType);
		return equipmentType;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/equipmenttypes")
	ResponseEntity<?> addEquipmentType(@RequestBody EquipmentType input,
													ServletWebRequest request)
	{
		EquipmentType result = new EquipmentType(input.getEquipmentType());
		equipmentService.addEquipmentType(result);

		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getEquipmentTypeId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/equipmenttypes/{equipmentTypeId}")
	ResponseEntity<?> removeEquipmentType(@PathVariable Integer equipmentTypeId,
														ServletWebRequest request)
	{

		equipmentService.deleteEquipmentType(equipmentTypeId);
		return ResponseEntity.noContent().build();
	}


	/*
	 * Equipment requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/equipment")
	Collection<EquipmentDTO> readEquipment(ServletWebRequest request) 
	{
		// generate set of EquipmentDTO from set of equipment
		return this.equipmentService.getAllEquipment().stream().map(EquipmentDTO::new).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/equipment/{equipmentId}")
	EquipmentDTO readEquipment(@PathVariable Integer equipmentId, 
											 ServletWebRequest request) 
	{
		return new EquipmentDTO(this.equipmentService.getEquipment(equipmentId));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/equipment/{equipmentId}")
	EquipmentDTO saveEquipment(@PathVariable Integer equipmentId, 
							   @RequestBody EquipmentDTO input,
							   				ServletWebRequest request) 
	{
		//ensure update is to the URL specified equipment id
		input.setEquipmentId(equipmentId);

		Equipment result = createEquipment(input);
		equipmentService.addEquipment(result);
		return new EquipmentDTO(result);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/equipment")
	ResponseEntity<?> addEquipment(@RequestBody EquipmentDTO input,
												ServletWebRequest request)
	{
		Equipment result = createEquipment(input);
		equipmentService.addEquipment(result);
			     
		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getEquipmentId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/equipment/{equipmentId}")
	ResponseEntity<?> removeEquipment(@PathVariable Integer equipmentId,
													ServletWebRequest request)
	{
		equipmentService.deleteEquipment(equipmentId);
		return ResponseEntity.noContent().build();
	}

	/*
	 * Equipment Component requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/equipment/{equipmentId}/components")
	Collection<EquipmentComponentDTO> readAssociatedEquipmentComponent(@PathVariable Integer equipmentId,
																					ServletWebRequest request) 
	{
		// generate set of EquipmentComponentDTO from set of equipmentComponents
		return this.equipmentService.getEquipmentComponents(equipmentId).stream()
											.map(EquipmentComponentDTO::new)
											.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/equipment/components/{componentId}")
	EquipmentComponentDTO readEquipmentComponent(@PathVariable Long componentId,
															   ServletWebRequest request) 
	{
		return new EquipmentComponentDTO(this.equipmentService.getEquipmentComponent(componentId));
	}


	@RequestMapping(method = RequestMethod.PUT, value = "/equipment/components/{componentId}")
	EquipmentComponentDTO saveEquipmentComponent(@PathVariable Long componentId, 
							        			 @RequestBody EquipmentComponentDTO input,
							   								  ServletWebRequest request) 
	{
		//ensure update is to the URL specified equipment component id
		input.setEquipmentComponentId(componentId);

		EquipmentComponent result = createEquipmentComponent(input);
		equipmentService.addEquipmentComponent(result);
		return new EquipmentComponentDTO(result);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/equipment/{equipmentId}/components")
	ResponseEntity<?> addEquipmentComponent(@PathVariable Integer equipmentId,
											@RequestBody EquipmentComponentDTO input,
														 ServletWebRequest request)
	{
		//ensure update is to the URL specified equipment id
		input.setEquipmentId(equipmentId);

		EquipmentComponent result = createEquipmentComponent(input);
		equipmentService.addEquipmentComponent(result);
			     
		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest()
							.replacePath("/cvpt/equipment/components").path("/{id}")
							.buildAndExpand(result.getEquipmentComponentId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/equipment/components/{componentId}")
	ResponseEntity<?> removeEquipmentComponent(@PathVariable Long componentId,
															 ServletWebRequest request)
	{
		equipmentService.deleteEquipmentComponent(componentId);
		return ResponseEntity.noContent().build();
	}


	/*
	 * Equipment Component Type requests
	 *
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/equipment/componenttypes")
	Collection<EquipmentComponentType> readEquipmentComponentType(ServletWebRequest request) 
	{
		return this.equipmentService.getAllEquipmentComponentTypes();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/equipment/componenttypes/{componentTypeId}")
	EquipmentComponentType readEquipmentComponentType(@PathVariable Integer componentTypeId, 
												  ServletWebRequest request) 
	{
		return this.equipmentService.getEquipmentComponentType(componentTypeId);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/equipment/componenttypes/{componentTypeId}")
	EquipmentComponentType saveEquipmentComponentType(@PathVariable Integer componentTypeId, 
													  @RequestBody EquipmentComponentType input,
									             				   ServletWebRequest request) 
	{
		EquipmentComponentType equipmentComponentType = new EquipmentComponentType(componentTypeId, 
																				   input.getEquipmentComponentType());
		equipmentService.addEquipmentComponentType(equipmentComponentType);
		return equipmentComponentType;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/equipment/componenttypes")
	ResponseEntity<?> addEquipmentComponentType(@RequestBody EquipmentComponentType input,
															 ServletWebRequest request)
	{
		EquipmentComponentType result = new EquipmentComponentType(input.equipmentComponentType);
		equipmentService.addEquipmentComponentType(result);

		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getEquipmentComponentTypeId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/equipment/componenttypes/{componentTypeId}")
	ResponseEntity<?> removeEquipmentComponentType(@PathVariable Integer componentTypeId,
																 ServletWebRequest request)
	{

		equipmentService.deleteEquipmentComponentType(componentTypeId);
		return ResponseEntity.noContent().build();
	}


	/*
	 * Exception handling
	 *
	 */
	@ExceptionHandler(RecordNotFoundException.class)
	ResponseEntity<?> recordNotFound(RecordNotFoundException e)
	{
		return ResponseEntity.notFound().header("Error", e.getMessage()).build();
	}

	/**
	 * Creates an Equipment model object given an EquipmentDTO. 
	 *
	 * @param equipmentDTO - holds the equipment data 
	 * 
	 * @return Equipment the created equipment
	 */
	private Equipment createEquipment(EquipmentDTO equipmentDTO)
	{
		Equipment equipment = new Equipment();

		if (equipmentDTO.equipmentIdExists())
		{
			equipment.setEquipmentId(equipmentDTO.getEquipmentId());
		}
		equipment.setDescription(equipmentDTO.getDescription());
		equipment.setAssetId(equipmentDTO.getAssetId());
		
		EquipmentType equipmentType = equipmentService.getEquipmentType(equipmentDTO.getEquipmentTypeId());
		equipment.setEquipmentType(equipmentType);

		equipment.setSerialNumber(equipmentDTO.getSerialNumber());
		equipment.setModelNumber(equipmentDTO.getModelNumber());
		equipment.setWan(equipmentDTO.getWan());

		if (equipmentDTO.latitudeExists()) 
		{
			equipment.setLatitude(equipmentDTO.getLatitude());
			equipment.setLongitude(equipmentDTO.getLongitude());
			equipment.setElevation(equipmentDTO.getElevation());
			equipment.setHeight(equipmentDTO.getHeight());
		}

		// only attempt to parse the date if it is not null
		if(equipmentDTO.getDateInstalled() == null)
		{
			equipment.setDateInstalled(null);
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date dateRead;
			try {
				dateRead = df.parse(equipmentDTO.getDateInstalled());
			} catch (ParseException pex) {
				throw new IllegalArgumentException("Incorrect date format: " + equipmentDTO.getDateInstalled() + " format should be yyyy-MM-dd");
			}
			equipment.setDateInstalled(dateRead);
		}
		
		equipment.setNotes(equipmentDTO.getNotes());

		return equipment;
	}

	/**
	 * Creates an EquipmentComponent model object given an 
	 * EquipmentComponentDTO. 
	 *
	 * @param equipmentComponentDTO - holds the equipment component data 
	 * 
	 * @return EquipmentComponent the created equipment component
	 */
	private EquipmentComponent createEquipmentComponent(EquipmentComponentDTO componentDTO)
	{
		EquipmentComponent component = new EquipmentComponent();

		if (componentDTO.equipmentComponentIdExists())
		{
			component.setEquipmentComponentId(componentDTO.getEquipmentComponentId());
		}
		EquipmentComponentType componentType = equipmentService.getEquipmentComponentType(componentDTO.getEquipmentComponentTypeId());
		component.setEquipmentComponentType(componentType);
		component.setEquipmentId(componentDTO.getEquipmentId());
		component.setDescription(componentDTO.getDescription());
		component.setSerialNumber(componentDTO.getSerialNumber());
		component.setModelNumber(componentDTO.getModelNumber());
		component.setVersion(componentDTO.getVersion());
		component.setCount(componentDTO.getCount());

		return component;
	}


}
