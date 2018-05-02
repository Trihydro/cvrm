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
import java.util.stream.Collectors;

import com.trihydro.cvpt.model.*;
import com.trihydro.cvpt.model.service.TrainingService;
import com.trihydro.cvpt.controller.dto.TrainingDTO;
import com.trihydro.cvpt.exceptions.RecordNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/cvpt")
class TrainingController 
{
	private final TrainingService trainingService;
	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	TrainingController(TrainingService trainingService) 
	{
		this.trainingService = trainingService;
	}


	/*
	 * Training Types requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/trainingtypes")
	Collection<TrainingType> readTrainingType(ServletWebRequest request) 
	{
		return this.trainingService.getAllTrainingTypes();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/trainingtypes/{trainingTypeId}")
	TrainingType readTrainingType(@PathVariable Integer trainingTypeId, 
											    ServletWebRequest request) 
	{
		return this.trainingService.getTrainingType(trainingTypeId);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/trainingtypes/{trainingTypeId}")
	TrainingType saveTrainingType(@PathVariable Integer trainingTypeId, 
								  @RequestBody TrainingType input,
									          ServletWebRequest request) 
	{
		TrainingType trainingType = new TrainingType(trainingTypeId, input.getTrainingType());
		trainingService.addTrainingType(trainingType);
		return trainingType;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/trainingtypes")
	ResponseEntity<?> addTrainingType(@RequestBody TrainingType input,
												  ServletWebRequest request)
	{
		TrainingType result = new TrainingType(input.getTrainingType());
		trainingService.addTrainingType(result);

		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getTrainingTypeId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/trainingtypes/{trainingTypeId}")
	ResponseEntity<?> removeTrainingType(@PathVariable Integer trainingTypeId,
													   ServletWebRequest request)
	{
		trainingService.deleteTrainingType(trainingTypeId);
		return ResponseEntity.noContent().build();
	}



	/*
	 * Training requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/trainings")
	Collection<TrainingDTO> readTraining(ServletWebRequest request) 
	{
		// generate set of TrainingDTO from set of trainings
		return this.trainingService.getAllTrainings().stream().map(TrainingDTO::new).collect(Collectors.toList());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/trainings/{trainingId}")
	TrainingDTO readTraining(@PathVariable Integer trainingId, 
										 ServletWebRequest request) 
	{
		return new TrainingDTO(this.trainingService.getTraining(trainingId));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/trainings/{trainingId}")
	TrainingDTO saveTraining(@PathVariable Integer trainingId, 
							   @RequestBody TrainingDTO input,
							   				ServletWebRequest request) 
	{
		//ensure update is to the URL specified training id
		input.setTrainingId(trainingId);

		Training result = createTraining(input);
		trainingService.addTraining(result);
		return new TrainingDTO(result);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/trainings")
	ResponseEntity<?> addTraining(@RequestBody TrainingDTO input,
											  ServletWebRequest request)
	{
		Training result = createTraining(input);
		trainingService.addTraining(result);
			     
		URI location = ServletUriComponentsBuilder
							.fromCurrentRequest().path("/{id}")
							.buildAndExpand(result.getTrainingId())
							.toUri();
		return ResponseEntity.created(location).build();
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/trainings/{trainingId}")
	ResponseEntity<?> removeTraining(@PathVariable Integer trainingId,
												  ServletWebRequest request)
	{
		trainingService.deleteTraining(trainingId);
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
	 * Creates a Training model object given a TrainingDTO. 
	 *
	 * @param trainingDTO - holds the training data 
	 * 
	 * @return Training the created training
	 */
	private Training createTraining(TrainingDTO trainingDTO)
	{
		Training training = new Training();

		if (trainingDTO.trainingIdExists())
		{
			training.setTrainingId(trainingDTO.getTrainingId());
		}
		training.setTraining(trainingDTO.getTraining());
		training.setCourseId(trainingDTO.getCourseId());

		TrainingType trainingType = trainingService.getTrainingType(trainingDTO.getTrainingTypeId());
		training.setTrainingType(trainingType);

		training.setNotes(trainingDTO.getNotes());

		return training;
	}

}
