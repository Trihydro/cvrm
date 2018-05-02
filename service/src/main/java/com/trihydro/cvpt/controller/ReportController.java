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
import com.trihydro.cvpt.model.service.ReportService;


@CrossOrigin
@RestController
@RequestMapping("/cvpt")
class ReportController 
{
	private final ReportService reportService;
	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	ReportController(ReportService reportService) 
	{
		this.reportService = reportService;
	}


	/*
	 * Vehicle Organization Report request
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/reports/vehicleorganizations")
	Collection<RecordVehicleOrganization> readRecordVehicleOrganization(ServletWebRequest request) 
	{
		return this.reportService.getVehicleOrganizationRecords();
	}

	/*
	 * Equipment Deployed Report request
	 *
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/reports/equipmentdeployed")
	Collection<RecordPercentCount> readRecordEquipmentDeployed(ServletWebRequest request) 
	{
		return this.reportService.getEquipmentDeployedRecords();
	}

	/*
	 * Participant Training Report request
	 *
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/reports/participanttraining")
	Collection<RecordCount> readRecordParticipantTraining(ServletWebRequest request) 
	{
		return this.reportService.getParticipantTrainingRecords();
	}



	/**
	 * Handles general exceptions thrown from any request
	 * mapping endpoints defined in this controller
	 *
	 * @param e - the thrown Exception
	 */
	@ExceptionHandler(Exception.class)
	ResponseEntity<?> errorOccured(Exception e)
	{
		return ResponseEntity.notFound().header("Error", e.getMessage()).build();
	}

}
