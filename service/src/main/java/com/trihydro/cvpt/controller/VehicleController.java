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
import java.util.Collection;
//import java.util.Iterator;
import java.util.stream.Collectors;

import com.trihydro.cvpt.model.*;
import com.trihydro.cvpt.model.service.EquipmentService;
import com.trihydro.cvpt.model.service.ParticipantService;
import com.trihydro.cvpt.model.service.VehicleService;
import com.trihydro.cvpt.controller.dto.EquipmentDTO;
import com.trihydro.cvpt.controller.dto.VehicleDTO;
import com.trihydro.cvpt.controller.dto.VehicleEquipmentDTO;
import com.trihydro.cvpt.exceptions.RecordNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/cvpt")
class VehicleController 
{
	private final VehicleService vehicleService;
	private final ParticipantService participantService;
	private final EquipmentService equipmentService;
	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	VehicleController(VehicleService vehicleService,
					  ParticipantService participantService,
					  EquipmentService equipmentService) 
	{
		this.vehicleService = vehicleService;
		this.participantService = participantService;
		this.equipmentService = equipmentService;
	}


	/*
	 * Vehicle Classes requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/vehicleclasses")
	Collection<VehicleClass> readVehicleClass(ServletWebRequest request) 
	{
		return this.vehicleService.getAllVehicleClasses();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/vehicleclasses/{vehicleClassId}")
	VehicleClass readVehicleClass(@PathVariable Integer vehicleClassId, 
											    ServletWebRequest request) 
	{
		return this.vehicleService.getVehicleClass(vehicleClassId);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/vehicleclasses/{vehicleClassId}")
	VehicleClass saveVehicleClass(@PathVariable Integer vehicleClassId, 
								  @RequestBody VehicleClass input,
									          ServletWebRequest request) 
	{
		VehicleClass vehicleClass = new VehicleClass(vehicleClassId, 
													 input.getVehicleClassNumber(),
													 input.getVehicleClass());
		vehicleService.addVehicleClass(vehicleClass);
		return vehicleClass;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/vehicleclasses")
	ResponseEntity<?> addVehicleClass(@RequestBody VehicleClass input,
												  ServletWebRequest request)
	{
		VehicleClass result = new VehicleClass(input.getVehicleClassNumber(),
											   input.getVehicleClass());
		vehicleService.addVehicleClass(result);

		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getVehicleClassId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/vehicleclasses/{vehicleClassId}")
	ResponseEntity<?> removeVehicleClass(@PathVariable Integer vehicleClassId,
													   ServletWebRequest request)
	{
		vehicleService.deleteVehicleClass(vehicleClassId);
		return ResponseEntity.noContent().build();
	}



	/*
	 * Vehicle requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/vehicles")
	Collection<VehicleDTO> readVehicle(ServletWebRequest request) 
	{
		// generate set of VehicleDTO from set of vehicles
		return this.vehicleService.getAllVehicles().stream().map(VehicleDTO::new).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/vehicles/{vehicleId}")
	VehicleDTO readVehicle(@PathVariable Integer vehicleId, 
										 ServletWebRequest request) 
	{
		return new VehicleDTO(this.vehicleService.getVehicle(vehicleId));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/vehicles/{vehicleId}")
	VehicleDTO saveVehicle(@PathVariable Integer vehicleId, 
							   @RequestBody VehicleDTO input,
							   				ServletWebRequest request) 
	{
		//ensure update is to the URL specified vehicle id
		input.setVehicleId(vehicleId);

		Vehicle result = createVehicle(input);
		vehicleService.addVehicle(result);
		return new VehicleDTO(result);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/vehicles")
	ResponseEntity<?> addVehicle(@RequestBody VehicleDTO input,
											  ServletWebRequest request)
	{
		Vehicle result = createVehicle(input);
		vehicleService.addVehicle(result);
			     
		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getVehicleId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/vehicles/{vehicleId}")
	ResponseEntity<?> removeVehicle(@PathVariable Integer vehicleId,
												  ServletWebRequest request)
	{
		vehicleService.deleteVehicle(vehicleId);
		return ResponseEntity.noContent().build();
	}

	/*
	 * Vehicle Equipment requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/vehicles/{vehicleId}/equipment")
	Collection<VehicleEquipmentDTO> readAssociatedVehicleEquipment(@PathVariable Integer vehicleId,
																		   ServletWebRequest request) 
	{
		// generate set of VehicleEquipmentDTO from set of vehicleEquipment
		return this.vehicleService.getAssociatedVehicleEquipment(vehicleId)
										.stream()
										.map(VehicleEquipmentDTO::new)
										.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/vehicles/{vehicleId}/availableequipment")
	Collection<EquipmentDTO> readAvailableVehicleEquipment(@PathVariable Integer vehicleId,
															ServletWebRequest request) 
	{
		// vehicleId is not currently used
		// generate set of EquipmentDTO from set of Equipment
		return this.equipmentService.getAvailableEquipmentForVehicle()
										.stream()
										.map(EquipmentDTO::new)
										.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/vehicles/equipment/{vehicleEquipmentId}")
	VehicleEquipmentDTO readVehicleEquipment(@PathVariable Integer vehicleEquipmentId,
											    		       ServletWebRequest request) 
	{
		return new VehicleEquipmentDTO(this.vehicleService.getVehicleEquipment(vehicleEquipmentId));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/vehicles/equipment/{vehicleEquipmentId}")
	VehicleEquipmentDTO saveVehicleEquipment(@PathVariable Integer vehicleEquipmentId, 
							        	     @RequestBody VehicleEquipmentDTO input,
							   								  ServletWebRequest request) 
	{
		//ensure update is to the URL specified vehicle equipment id
		input.setVehicleEquipmentId(vehicleEquipmentId);

		VehicleEquipment result = createVehicleEquipment(input);
		vehicleService.addVehicleEquipment(result);
		return new VehicleEquipmentDTO(result);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/vehicles/{vehicleId}/equipment")
	ResponseEntity<?> addVehicleEquipment(@PathVariable Integer vehicleId,
										  @RequestBody VehicleEquipmentDTO input,
													   ServletWebRequest request)
	{
		//ensure update is to the URL specified vehicle id
		input.setVehicleId(vehicleId);

		VehicleEquipment result = createVehicleEquipment(input);
		vehicleService.addVehicleEquipment(result);
			     
		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest()
							.replacePath("/cvpt/vehicles/equipment").path("/{id}")
							.buildAndExpand(result.getVehicleEquipmentId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/vehicles/equipment/{vehicleEquipmentId}")
	ResponseEntity<?> removeVehicleEquipment(@PathVariable Integer vehicleEquipmentId,
														   ServletWebRequest request)
	{
		vehicleService.deleteVehicleEquipment(vehicleEquipmentId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * Handles RecordNotFoundException thrown from any request
	 * mapping endpoints defined in this controller
	 *
	 * @param e - the thrown RecordNotFoundException
	 */
	@ExceptionHandler(RecordNotFoundException.class)
	ResponseEntity<?> recordNotFound(RecordNotFoundException e)
	{
		return ResponseEntity.notFound().header("Error", e.getMessage()).build();
	}

	/**
	 * Creates a Vehicle model object given a VehicleDTO. 
	 *
	 * @param vehicleDTO - holds the vehicle data 
	 * 
	 * @return Vehicle the created vehicle
	 */
	private Vehicle createVehicle(VehicleDTO vehicleDTO)
	{
		Vehicle vehicle = new Vehicle();

		if (vehicleDTO.vehicleIdExists())
		{
			vehicle.setVehicleId(vehicleDTO.getVehicleId());
		}
		vehicle.setId(vehicleDTO.getId());

		VehicleClass vehicleClass = vehicleService.getVehicleClass(vehicleDTO.getVehicleClassId());
		vehicle.setVehicleClass(vehicleClass);
		Organization organization = participantService.getOrganization(vehicleDTO.getOrganizationId());
		vehicle.setOrganization(organization);
		
		vehicle.setVin(vehicleDTO.getVin());
		vehicle.setMake(vehicleDTO.getMake());
		vehicle.setModel(vehicleDTO.getModel());
		vehicle.setNotes(vehicleDTO.getNotes());

		return vehicle;
	}

	/**
	 * Creates a VehicleEquipment model object given a VehicleEquipmentDTO. 
	 *
	 * @param vehicleEquipmentDTO - holds the vehicle equipment data 
	 * 
	 * @return VehicleEquipment the created vehicle equipment relation
	 */
	private VehicleEquipment createVehicleEquipment(VehicleEquipmentDTO veDTO)
	{
		VehicleEquipment vehicleEquipment = new VehicleEquipment();

		if (veDTO.vehicleEquipmentIdExists())
		{
			vehicleEquipment.setVehicleEquipmentId(veDTO.getVehicleEquipmentId());
		}
		vehicleEquipment.setVehicleId(veDTO.getVehicleId());

		Equipment equipment = equipmentService.getEquipment(veDTO.getEquipmentId());
		vehicleEquipment.setEquipment(equipment);
		
		return vehicleEquipment;
	}
}
