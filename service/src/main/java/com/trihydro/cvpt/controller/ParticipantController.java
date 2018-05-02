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
import com.trihydro.cvpt.model.service.ParticipantService;
import com.trihydro.cvpt.model.service.TrainingService;
import com.trihydro.cvpt.model.service.VehicleService;
import com.trihydro.cvpt.controller.dto.ParticipantDTO;
import com.trihydro.cvpt.controller.dto.ParticipantTrainingDTO;
import com.trihydro.cvpt.controller.dto.ParticipantVehicleDTO;
import com.trihydro.cvpt.controller.dto.TrainingDTO;
import com.trihydro.cvpt.controller.dto.VehicleDTO;
import com.trihydro.cvpt.exceptions.RecordNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/cvpt")
class ParticipantController 
{
	private final ParticipantService participantService;
	private final VehicleService vehicleService;
	private final TrainingService trainingService;
	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	ParticipantController(ParticipantService participantService,
						  VehicleService vehicleService,
						  TrainingService trainingService) 
	{
		this.participantService = participantService;
		this.vehicleService = vehicleService;
		this.trainingService = trainingService;
	}


	/*
	 * Organization requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/organizations")
	Collection<Organization> readOrganization(ServletWebRequest request) 
	{
		return this.participantService.getAllOrganizations();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/organizations/{organizationId}")
	Organization readOrganization(@PathVariable Integer organizationId, 
												  ServletWebRequest request) 
	{
		return this.participantService.getOrganization(organizationId);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/organizations/{organizationId}")
	Organization saveOrganization(@PathVariable Integer organizationId, 
								  @RequestBody Organization input,
									           ServletWebRequest request)
	{
		Organization organization = new Organization(organizationId, 
			                                         input.getName(),
			                                         input.getIsTruckingCompany());
		participantService.addOrganization(organization);
		return organization;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/organizations")
	ResponseEntity<?> addOrganization(@RequestBody Organization input,
												   ServletWebRequest request)
	{
		Organization result = new Organization(input.getName(), 
											   input.getIsTruckingCompany());
		participantService.addOrganization(result);

		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getOrganizationId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/organizations/{organizationId}")
	ResponseEntity<?> removeOrganization(@PathVariable Integer organizationId,
														ServletWebRequest request)
	{

		participantService.deleteOrganization(organizationId);
		return ResponseEntity.noContent().build();
	}


	/*
	 * Participant requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/participants")
	Collection<ParticipantDTO> readParticipant(ServletWebRequest request) 
	{
		// generate set of ParticipantDTO from set of participants
		return this.participantService.getAllParticipants().stream().map(ParticipantDTO::new).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/participants/{participantId}")
	ParticipantDTO readParticipant(@PathVariable Integer participantId, 
											    ServletWebRequest request) 
	{
		return new ParticipantDTO(this.participantService.getParticipant(participantId));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/participants/{participantId}")
	ParticipantDTO saveParticipant(@PathVariable Integer participantId, 
							        @RequestBody ParticipantDTO input,
							   				ServletWebRequest request) 
	{
		//ensure update is to the URL specified participant id
		input.setParticipantId(participantId);

		Participant result = createParticipant(input);
		participantService.addParticipant(result);
		return new ParticipantDTO(result);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/participants")
	ResponseEntity<?> addParticipant(@RequestBody ParticipantDTO input,
												ServletWebRequest request)
	{
		Participant result = createParticipant(input);
		participantService.addParticipant(result);
			     
		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getParticipantId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/participants/{participantId}")
	ResponseEntity<?> removeParticipant(@PathVariable Integer participantId,
													ServletWebRequest request)
	{
		participantService.deleteParticipant(participantId);
		return ResponseEntity.noContent().build();
	}

	/*
	 * Participant vehicle requests
	 *
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/participants/{participantId}/vehicles")
	Collection<ParticipantVehicleDTO> readAssociatedParticipantVehicle(@PathVariable Integer participantId,
																		   ServletWebRequest request) 
	{
		// generate set of ParticipantVehicleDTO from set of participantVehicles
		return this.participantService.getParticipantVehicles(participantId)
										.stream()
										.map(ParticipantVehicleDTO::new)
										.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/participants/{participantId}/availablevehicles")
	Collection<VehicleDTO> readAvailableParticipantVehicle(@PathVariable Integer participantId,
																		  ServletWebRequest request) 
	{
		// generate set of VehicleDTO from set of Vehicles
		return this.vehicleService.getAvailableVehiclesForParticipant(participantId)
										.stream()
										.map(VehicleDTO::new)
										.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/participants/vehicles/{participantVehicleId}")
	ParticipantVehicleDTO readParticipantVehicle(@PathVariable Integer participantVehicleId,
											    		       ServletWebRequest request) 
	{
		return new ParticipantVehicleDTO(this.participantService.getParticipantVehicle(participantVehicleId));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/participants/vehicles/{participantVehicleId}")
	ParticipantVehicleDTO saveParticipantVehicle(@PathVariable Integer participantVehicleId, 
							        			 @RequestBody ParticipantVehicleDTO input,
							   								  ServletWebRequest request) 
	{
		//ensure update is to the URL specified participant vehicle id
		input.setParticipantVehicleId(participantVehicleId);

		ParticipantVehicle result = createParticipantVehicle(input);
		participantService.addParticipantVehicle(result);
		return new ParticipantVehicleDTO(result);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/participants/{participantId}/vehicles")
	ResponseEntity<?> addParticipantVehicle(@PathVariable Integer participantId,
											@RequestBody ParticipantVehicleDTO input,
														 ServletWebRequest request)
	{
		//ensure update is to the URL specified participant id
		input.setParticipantId(participantId);

		ParticipantVehicle result = createParticipantVehicle(input);
		participantService.addParticipantVehicle(result);
			     
		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest()
							.replacePath("/cvpt/participants/vehicles").path("/{id}")
							.buildAndExpand(result.getParticipantVehicleId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/participants/vehicles/{participantVehicleId}")
	ResponseEntity<?> removeParticipantVehicle(@PathVariable Integer participantVehicleId,
															 ServletWebRequest request)
	{
		participantService.deleteParticipantVehicle(participantVehicleId);
		return ResponseEntity.noContent().build();
	}

	/*
	 * Participant training requests
	 *
	 */

	@RequestMapping(method = RequestMethod.GET, value = "/participants/{participantId}/trainings")
	Collection<ParticipantTrainingDTO> readAssociatedParticipantTraining(@PathVariable Integer participantId,
																		   ServletWebRequest request) 
	{
		// generate set of ParticipantTrainingDTO from set of participantTrainings
		return this.participantService.getParticipantTrainings(participantId)
										.stream()
										.map(ParticipantTrainingDTO::new)
										.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/participants/{participantId}/availabletrainings")
	Collection<TrainingDTO> readAvailableParticipantTraining(@PathVariable Integer participantId,
																		  ServletWebRequest request) 
	{
		// generate set of TrainingDTO from set of Trainings
		return this.trainingService.getAvailableTrainingsForParticipant(participantId)
										.stream()
										.map(TrainingDTO::new)
										.collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/participants/trainings/{participantTrainingId}")
	ParticipantTrainingDTO readParticipantTraining(@PathVariable Integer participantTrainingId,
											    		       ServletWebRequest request) 
	{
		return new ParticipantTrainingDTO(this.participantService.getParticipantTraining(participantTrainingId));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/participants/trainings/{participantTrainingId}")
	ParticipantTrainingDTO saveParticipantTraining(@PathVariable Integer participantTrainingId, 
							        			 @RequestBody ParticipantTrainingDTO input,
							   								  ServletWebRequest request) 
	{
		//ensure update is to the URL specified participant training id
		input.setParticipantTrainingId(participantTrainingId);

		ParticipantTraining result = createParticipantTraining(input);
		participantService.addParticipantTraining(result);
		return new ParticipantTrainingDTO(result);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/participants/{participantId}/trainings")
	ResponseEntity<?> addParticipantTraining(@PathVariable Integer participantId,
											@RequestBody ParticipantTrainingDTO input,
														 ServletWebRequest request)
	{
		//ensure update is to the URL specified participant id
		input.setParticipantId(participantId);

		ParticipantTraining result = createParticipantTraining(input);
		participantService.addParticipantTraining(result);
			     
		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest()
							.replacePath("/cvpt/participants/trainings").path("/{id}")
							.buildAndExpand(result.getParticipantTrainingId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/participants/trainings/{participantTrainingId}")
	ResponseEntity<?> removeParticipantTraining(@PathVariable Integer participantTrainingId,
												 ServletWebRequest request)
	{
		participantService.deleteParticipantTraining(participantTrainingId);
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
	 * Creates a Participant model object given a ParticipantDTO. 
	 *
	 * @param participantDTO - holds the participant data 
	 * 
	 * @return Participant the created participant
	 */
	private Participant createParticipant(ParticipantDTO participantDTO)
	{
		Participant participant = new Participant();

		if (participantDTO.participantIdExists())
		{
			participant.setParticipantId(participantDTO.getParticipantId());
		}
		participant.setFirstName(participantDTO.getFirstName());
		participant.setLastName(participantDTO.getLastName());
		participant.setEmail(participantDTO.getEmail());
		
		Organization organization = participantService.getOrganization(participantDTO.getOrganizationId());
		participant.setOrganization(organization);

		// only attempt to parse the start date if it is not null
		if(participantDTO.getStartDate() == null)
		{
			participant.setStartDate(null);
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date dateRead;
			try {
				dateRead = df.parse(participantDTO.getStartDate());
			} catch (ParseException pex) {
				throw new IllegalArgumentException("Incorrect date format: " + participantDTO.getStartDate() + " format should be yyyy-MM-dd");
			}
			participant.setStartDate(dateRead);
		}

		// only attempt to parse the end date if it is not null
		if(participantDTO.getEndDate() == null)
		{
			participant.setEndDate(null);
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date dateRead;
			try {
				dateRead = df.parse(participantDTO.getEndDate());
			} catch (ParseException pex) {
				throw new IllegalArgumentException("Incorrect date format: " + participantDTO.getEndDate() + " format should be yyyy-MM-dd");
			}
			participant.setEndDate(dateRead);
		}

		return participant;
	}

	/**
	 * Creates a ParticipantVehicle model object given a ParticipantVehicleDTO. 
	 *
	 * @param participantVehicleDTO - holds the participant vehicle data 
	 * 
	 * @return ParticipantVehicle the created participant vehicle relation
	 */
	private ParticipantVehicle createParticipantVehicle(ParticipantVehicleDTO pvDTO)
	{
		ParticipantVehicle participantVehicle = new ParticipantVehicle();

		if (pvDTO.participantVehicleIdExists())
		{
			participantVehicle.setParticipantVehicleId(pvDTO.getParticipantVehicleId());
		}
		participantVehicle.setParticipantId(pvDTO.getParticipantId());
		Vehicle vehicle = vehicleService.getVehicle(pvDTO.getVehicleId());
		participantVehicle.setVehicle(vehicle);
		participantVehicle.setIsPrimary(pvDTO.getIsPrimary());

		return participantVehicle;
	}

	/**
	 * Creates a ParticipantTraining model object given a ParticipantTrainingDTO. 
	 *
	 * @param participantTrainingDTO - holds the participant training data 
	 * 
	 * @return ParticipantTraining the created participant training relation
	 */
	private ParticipantTraining createParticipantTraining(ParticipantTrainingDTO ptDTO)
	{
		ParticipantTraining participantTraining = new ParticipantTraining();

		if (ptDTO.participantTrainingIdExists())
		{
			participantTraining.setParticipantTrainingId(ptDTO.getParticipantTrainingId());
		}
		participantTraining.setParticipantId(ptDTO.getParticipantId());
		Training training = trainingService.getTraining(ptDTO.getTrainingId());
		participantTraining.setTraining(training);
		participantTraining.setTimeToComplete(ptDTO.getTimeToComplete());

		// only attempt to parse the date if it is not null
		if(ptDTO.getDateCompleted() == null)
		{
			participantTraining.setDateCompleted(null);
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date dateRead;
			try {
				dateRead = df.parse(ptDTO.getDateCompleted());
			} catch (ParseException pex) {
				throw new IllegalArgumentException("Incorrect date format: " + ptDTO.getDateCompleted() + " format should be yyyy-MM-dd");
			}
			participantTraining.setDateCompleted(dateRead);
		}

		return participantTraining;
	}

}
