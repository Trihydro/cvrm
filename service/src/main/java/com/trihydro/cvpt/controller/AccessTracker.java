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


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.trihydro.cvpt.controller.dto.EquipmentDTO;
import com.trihydro.cvpt.controller.dto.EquipmentComponentDTO;
import com.trihydro.cvpt.controller.dto.ParticipantDTO;
import com.trihydro.cvpt.controller.dto.ParticipantTrainingDTO;
import com.trihydro.cvpt.controller.dto.ParticipantVehicleDTO;
import com.trihydro.cvpt.controller.dto.TrainingDTO;
import com.trihydro.cvpt.controller.dto.VehicleDTO;
import com.trihydro.cvpt.controller.dto.VehicleEquipmentDTO;
import com.trihydro.cvpt.model.EquipmentComponentType;
import com.trihydro.cvpt.model.EquipmentType;
import com.trihydro.cvpt.model.Log;
import com.trihydro.cvpt.model.Organization;
import com.trihydro.cvpt.model.TrainingType;
import com.trihydro.cvpt.model.User;
import com.trihydro.cvpt.model.UserPassword;
import com.trihydro.cvpt.model.service.UserService;
import com.trihydro.cvpt.repository.LogRepository;



/**
 * Implements advice to control and log access to all of the 
 * CV Resource Management data stored in the associated CV database.
 * Data redaction per user role is also implemented in the advice 
 * of specific REST endpoints.
 *
 */
@Component
@Aspect
public class AccessTracker 
{

	private final LogRepository logRepository;
	private final UserService userService;
	private String userLogin;
	private String userRole;
	private Pattern pattern;
	private Pattern idPattern;
	private static final String REDACT_TEXT = "X";

	/**
	 * Constructor - application context
	 */	
	@Autowired
	AccessTracker(LogRepository logRepository, 
				  UserService userService)
	{
		this.logRepository = logRepository;
		this.userService = userService;
		// regex pattern to find series of letters numbers and periods making up
		// a JWT (base64url encoded)
		this.pattern = Pattern.compile("[a-zA-Z_0-9.-]+"); 

		// regex pattern to find id number at end of url string
		this.idPattern = Pattern.compile("\\d+$");

		// identify a default user id and role for development testing
		this.userLogin = "testing@trihydro.com";
		this.userRole = "ROLE_ADMIN";
	}

	/* **********************
	 * Participant Controller
	 * **********************
	 */

	/**
	 * Handles access to the request for all participants. The
	 * AOP pointcut matches the REST endpoint readParticipant with a 
	 * single argument. 
	 *
	 * @param request - the httpRequest that drove this infromation request
	 * @param participantList - list of ParticipantDTO returned by the target method
	 *
	 */
	@AfterReturning(pointcut="within(ParticipantController) && execution(* readParticipant(*)) && args(request)", 
		            returning="participantList")
	public void allParticipantsAccess(ServletWebRequest request, Collection<ParticipantDTO> participantList)
	{
		// get request user to determine access
		User user = getUserFromRequest(request);
		List<String> roles = user.getRoles();
		boolean redact = true;

		// if user has admin or training roles then there is no redaction
		if(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_TRAINING")) {redact = false;}

		if(redact)
		{
			for(ParticipantDTO partDTO : participantList)
			{
				partDTO.setFirstName(REDACT_TEXT);
				partDTO.setLastName(REDACT_TEXT);
				partDTO.setEmail(REDACT_TEXT);
			}
		}

		// write log record 
		writeLogRecord(user.getEmail(), 
			           request.getHttpMethod().toString(), 
			           "participant");
	}

	/**
	 * Handles access to the request for a single participant. The
	 * AOP pointcut matches the REST endpoint readParticipant that 
	 * returns a single ParticipantDTO object. 
	 *
	 * @param request - the httpRequest that drove this infromation request
	 * @param partDTO - the ParticipantDTO returned by the target method
	 *
	 */
	@AfterReturning(pointcut="within(ParticipantController) && execution(com.trihydro.cvpt.controller.dto.ParticipantDTO readParticipant(..)) && args(..,request)",
					returning="partDTO")
	public void readParticipantAccess(ServletWebRequest request, ParticipantDTO partDTO) 
	{
		// get request user to determine access
		User user = getUserFromRequest(request);
		List<String> roles = user.getRoles();
		boolean redact = true;

		// if user has admin or training roles then there is no redaction
		if(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_TRAINING")) {redact = false;}

		if(redact)
		{
			partDTO.setFirstName(REDACT_TEXT);
			partDTO.setLastName(REDACT_TEXT);
			partDTO.setEmail(REDACT_TEXT);
		}

		// write new log record
		writeLogRecord(user.getEmail(), request.getHttpMethod().toString(), "participant");
	}

	/**
	 * Logs access to participant table from the addParticipant REST endpoint
	 *
	 * @param request - servlet request
	 * @param partDTO - the participantDTO with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.addParticipant(..)) && args(partDTO,request)",
		            returning="response")
	public void addParticipantAccess(ServletWebRequest request, ParticipantDTO partDTO, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "participant",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(partDTO));
	}

	/**
	 * Logs access to participant table from the saveParticipant REST endpoint
	 *
	 * @param request - servlet request
	 * @param org - the participant with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.saveParticipant(..)) && args(..,request)",
		            returning="partDTO")
	public void saveParticipantAccess(ServletWebRequest request, ParticipantDTO partDTO) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "participant",
			           partDTO.getParticipantId(),
			           generateJsonStringFromObject(partDTO));
	}

	/**
	 * Logs access to participant table from the removeParticipant REST endpoint
	 *
	 * @param request - servlet request
	 * @param orgId - the id for the participant that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.removeParticipant(..)) && args(partId,request)")
	public void removeParticipantAccess(ServletWebRequest request, Integer partId) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "participant",
			           partId,
			           "");
	}


	/**
	 * Logs access to organization table from the read organization REST
	 * endpoints, all organizations or a single organization.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(ParticipantController) && execution(* readOrganization(..)) && args(..,request)")
	public void readOrganizationAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "organization");
	}

	/**
	 * Logs access to organiztion table from the addOrganization REST 
	 * endpoint
	 *
	 * @param request - servlet request
	 * @param org - the organization with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.addOrganization(..)) && args(org,request)",
		            returning="response")
	public void addOrganizationAccess(ServletWebRequest request, Organization org, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "organization",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(org));
	}

	/**
	 * Logs access to orginzation table from the saveOrganization REST endpoint
	 *
	 * @param request - servlet request
	 * @param org - the organization with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.saveOrganization(..)) && args(..,request)",
		            returning="org")
	public void saveOrganizationAccess(ServletWebRequest request, Organization org) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "organization",
			           org.getOrganizationId(),
			           generateJsonStringFromObject(org));
	}

	/**
	 * Logs access to orginzation table from the removeOrganization REST endpoint
	 *
	 * @param request - servlet request
	 * @param orgId - the id for the organization that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.removeOrganization(..)) && args(orgId,request)")
	public void removeOrganizationAccess(ServletWebRequest request, Integer orgId) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "organization",
			           orgId,
			           "");
	}


	/**
	 * Handles access to the request for all participant vehicles. The
	 * AOP pointcut matches the REST endpoint readAssociatedParticipantVehicle
	 * returning a list of ParticipantVehicleDTO. 
	 *
	 * @param request - the httpRequest that drove this infromation request
	 * @param participantVehicleList - list of ParticipantVehicleDTO returned by the target method
	 *
	 */
	@AfterReturning(pointcut="within(ParticipantController) && execution(* readAssociatedParticipantVehicle(..)) && args(..,request)", 
		            returning="participantVehicleList")
	public void allParticipantVehiclesAccess(ServletWebRequest request, Collection<ParticipantVehicleDTO> participantVehicleList)
	{
		// get request user to determine access
		User user = getUserFromRequest(request);
		List<String> roles = user.getRoles();
		boolean redact = true;

		// if user has admin, training or vehicle roles then there is no redaction
		if(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_TRAINING") || roles.contains("ROLE_VEHICLE")) {redact = false;}

		if(redact)
		{
			for(ParticipantVehicleDTO pvDTO : participantVehicleList)
			{
				pvDTO.setId(REDACT_TEXT);
			}
		}

		// write log record 
		writeLogRecord(user.getEmail(), request.getHttpMethod().toString(), "participant_vehicle");
	}

	/**
	 * Handles access to the request for a single participant vehicle record.
	 * The AOP pointcut matches the REST endpoint readParticipantVehicle
	 * that returns a single ParticipantVehicleDTO object. 
	 *
	 * @param request - the httpRequest that drove this infromation request
	 * @param pvDTO - the ParticipantVehicleDTO returned by the target method
	 *
	 */
	@AfterReturning(pointcut="within(ParticipantController) && execution(com.trihydro.cvpt.controller.dto.ParticipantVehicleDTO readParticipantVehicle(..)) && args(..,request)",
					returning="pvDTO")
	public void readParticipantVehicleAccess(ServletWebRequest request, ParticipantVehicleDTO pvDTO) 
	{
		// get request user to determine access
		User user = getUserFromRequest(request);
		List<String> roles = user.getRoles();
		boolean redact = true;

		// if user has admin, training or vehicle roles then there is no redaction
		if(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_TRAINING") || roles.contains("ROLE_VEHICLE")) {redact = false;}

		if(redact)
		{
			pvDTO.setId(REDACT_TEXT);
		}

		// write new log record
		writeLogRecord(user.getEmail(), request.getHttpMethod().toString(), "participant_vehicle");
	}

	/**
	 * Logs access to vehicle table when requesting available vehicles
	 * for participant vehicle.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(ParticipantController) && execution(* readAvailableParticipantVehicle(..)) && args(..,request)")
	public void participantAvailableVehicleAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "vehicle");
	}


	/**
	 * Logs access to participant_vehicle table from the addParticipantVehicle
	 * REST endpoint
	 *
	 * @param request - servlet request
	 * @param pvDTO - the participantVehicle with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.addParticipantVehicle(..)) && args(..,pvDTO,request)",
		            returning="response")
	public void addParticipantVehicleAccess(ServletWebRequest request, ParticipantVehicleDTO pvDTO, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "participant_vehicle",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(pvDTO));
	}

	/**
	 * Logs access to participant_vehicle table from the saveParticipantVehicle
	 * REST endpoint
	 *
	 * @param request - servlet request
	 * @param pvDTO - the participantVehicle with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.saveParticipantVehicle(..)) && args(..,request)",
		            returning="pvDTO")
	public void saveParticipantVehicleAccess(ServletWebRequest request, ParticipantVehicleDTO pvDTO) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "participant_vehicle",
			           pvDTO.getParticipantVehicleId(),
			           generateJsonStringFromObject(pvDTO));
	}

	/**
	 * Logs access to participant_vehicle table from the removeParticipantVehicle
	 * REST endpoint
	 *
	 * @param request - servlet request
	 * @param pvId - the id for the participantVehicle that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.removeParticipantVehicle(..)) && args(pvId,request)")
	public void removeParticipantVehicleAccess(ServletWebRequest request, Integer pvId) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "participant_vehicle",
			           pvId,
			           "");
	}


	/**
	 * Logs access to training table when requesting available trainings
	 * for a participant.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(ParticipantController) && execution(* readAvailableParticipantTraining(..)) && args(..,request)")
	public void participantAvailableTrainingAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "training");
	}

	@Pointcut("execution(* com.trihydro.cvpt.controller.ParticipantController.readAssociatedParticipantTraining(..))" +
		" || execution(* com.trihydro.cvpt.controller.ParticipantController.readParticipantTraining(..))")
	private void readParticipantTrainingPointcut() {}

	/**
	 * Logs all access to participant_training table from the read 
	 * ParticipantTraining REST endpoints.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("readParticipantTrainingPointcut() && args(..,request)")
	public void readParticipantTrainingAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "participant_training");
	}

	/**
	 * Logs access to participant_training table from the addParticipantTraining
	 * REST endpoint
	 *
	 * @param request - servlet request
	 * @param ptDTO - the participantTraining with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.addParticipantTraining(..)) && args(..,ptDTO,request)",
		            returning="response")
	public void addParticipantTrainingAccess(ServletWebRequest request, ParticipantTrainingDTO ptDTO, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "participant_training",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(ptDTO));
	}

	/**
	 * Logs access to participant_training table from the saveParticipantTraining
	 * REST endpoint
	 *
	 * @param request - servlet request
	 * @param ptDTO - the participantTraining with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.saveParticipantTraining(..)) && args(..,request)",
		            returning="ptDTO")
	public void saveParticipantTrainingAccess(ServletWebRequest request, ParticipantTrainingDTO ptDTO) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "participant_training",
			           ptDTO.getParticipantTrainingId(),
			           generateJsonStringFromObject(ptDTO));
	}

	/**
	 * Logs access to participant_training table from the removeParticipantTraining
	 * REST endpoint
	 *
	 * @param request - servlet request
	 * @param ptId - the id for the participantTraining that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.ParticipantController.removeParticipantTraining(..)) && args(ptId,request)")
	public void removeParticipantTrainingAccess(ServletWebRequest request, Integer ptId) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "participant_training",
			           ptId,
			           "");
	}


	/* **********************
	 * Equipment Controller
	 * **********************
	 */

	/**
	 * Logs access to equipment_type table from the read equipment type REST
	 * endpoints, all equipment types or a single one.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(EquipmentController) && execution(* readEquipmentType(..)) && args(..,request)")
	public void readEquipmentTypeAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "equipment_type");
	}

	/**
	 * Logs access to equipment_type table from the addEquipmentType REST 
	 * endpoint
	 *
	 * @param request - servlet request
	 * @param eqType - the equipmentType with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.addEquipmentType(..)) && args(eqType,request)",
		            returning="response")
	public void addEquipmentTypeAccess(ServletWebRequest request, EquipmentType eqType, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment_type",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(eqType));
	}

	/**
	 * Logs access to equipment_type table from the saveEquipmentType REST 
	 * endpoint
	 *
	 * @param request - servlet request
	 * @param eqType - the equipmentType with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.saveEquipmentType(..)) && args(..,request)",
		            returning="eqType")
	public void saveEquipmentTypeAccess(ServletWebRequest request, EquipmentType eqType) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment_type",
			           eqType.getEquipmentTypeId(),
			           generateJsonStringFromObject(eqType));
	}

	/**
	 * Logs access to equipment_type table from the removeEquipmentType REST 
	 * endpoint
	 *
	 * @param request - servlet request
	 * @param eqTypeId - the id for the equipmentType that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.removeEquipmentType(..)) && args(eqTypeId,request)")
	public void removeEquipmentTypeAccess(ServletWebRequest request, Integer eqTypeId) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment_type",
			           eqTypeId,
			           "");
	}

	/**
	 * Logs access to equipment table from the read equipment REST
	 * endpoints, all equipment or a single one.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(EquipmentController) && execution(* readEquipment(..)) && args(..,request)")
	public void readEquipmentAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "equipment");
	}

	/**
	 * Logs access to equipment table from the addEquipment REST endpoint.
	 *
	 * @param request - servlet request
	 * @param eqDTO - the equipmentDTO with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.addEquipment(..)) && args(eqDTO,request)",
		            returning="response")
	public void addEquipmentAccess(ServletWebRequest request, EquipmentDTO eqDTO, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(eqDTO));
	}

	/**
	 * Logs access to equipment table from the saveEquipment REST endpoint.
	 *
	 * @param request - servlet request
	 * @param eqDTO - the equipmentDTO with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.saveEquipment(..)) && args(..,request)",
		            returning="eqDTO")
	public void saveEquipmentAccess(ServletWebRequest request, EquipmentDTO eqDTO) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment",
			           eqDTO.getEquipmentId(),
			           generateJsonStringFromObject(eqDTO));
	}

	/**
	 * Logs access to equipment table from the removeEquipment REST endpoint.
	 *
	 * @param request - servlet request
	 * @param id - the id for the equipment that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.removeEquipment(..)) && args(id,request)")
	public void removeEquipmentAccess(ServletWebRequest request, Integer id) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment",
			           id,
			           "");
	}


	@Pointcut("execution(* com.trihydro.cvpt.controller.EquipmentController.readAssociatedEquipmentComponent(..))" +
		" || execution(* com.trihydro.cvpt.controller.EquipmentController.readEquipmentComponent(..))")
	private void readEquipmentComponentPointcut() {}


	/**
	 * Logs access to equipment_component table from the read equipment component
	 * REST endpoints, equipment components for a specific equipment or a single
	 * specific equipment component. 
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("readEquipmentComponentPointcut() && args(..,request)")
	public void readEquipmentComponentAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "equipment_component");
	}

	/**
	 * Logs access to equipment_component table from the add equipment component
	 * REST endpoint.
	 * The equipment component id is a Long, but is handled here as an Integer
	 * this should be ok as the id is auto-numbered and should not exceed the
	 * integer MAX size for this application.
	 *
	 * @param request - servlet request
	 * @param ecDTO - the equipmentComponentDTO with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.addEquipmentComponent(..)) && args(..,ecDTO,request)",
		            returning="response")
	public void addEquipmentComponentAccess(ServletWebRequest request, EquipmentComponentDTO ecDTO, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment_component",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(ecDTO));
	}

	/**
	 * Logs access to equipment_component table from the save equipment component
	 * REST endpoint.
	 * The equipment component id is a Long, but is handled here as an Integer
	 * this should be ok as the id is auto-numbered and should not come close to
	 * exceeding the integer MAX size for this application.
	 *
	 * @param request - servlet request
	 * @param ecDTO - the equipmentComponentDTO with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.saveEquipmentComponent(..)) && args(..,request)",
		            returning="ecDTO")
	public void saveEquipmentComponentAccess(ServletWebRequest request, EquipmentComponentDTO ecDTO) 
	{
		// extract the equipment component id and covert to Integer
		// uses Java narrowing primitive conversion
		Integer id = ecDTO.getEquipmentComponentId().intValue();

		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment_component",
			           id,
			           generateJsonStringFromObject(ecDTO));
	}

	/**
	 * Logs access to equipment_component table from the remove equipment component
	 * REST endpoint.
	 * The equipment component id is a Long, but is handled here as an Integer
	 * this should be ok as the id is auto-numbered and should not come close to
	 * exceeding the integer MAX size for this application.
	 *
	 * @param request - servlet request
	 * @param lid - the id for the equipment component that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.removeEquipmentComponent(..)) && args(lid,request)")
	public void removeEquipmentComponentAccess(ServletWebRequest request, Long lid) 
	{
		// convert the equipment component id to Integer
		// uses Java narrowing primitive conversion
		Integer id = lid.intValue();

		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment_component",
			           id,
			           "");
	}

	/**
	 * Logs access to equipment_component_type table from the read equipment 
	 * component type REST endpoints, all equipment component types or a single one.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(EquipmentController) && execution(* readEquipmentComponentType(..)) && args(..,request)")
	public void readEquipmentComponentTypeAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "equipment_component_type");
	}

	/**
	 * Logs access to equipment_component_type table from the add equipment
	 * component type REST endpoint.
	 *
	 * @param request - servlet request
	 * @param ecType - the equipmentComponentType with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.addEquipmentComponentType(..)) && args(ecType,request)",
		            returning="response")
	public void addEquipmentComponentTypeAccess(ServletWebRequest request, EquipmentComponentType ecType, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment_component_type",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(ecType));
	}

	/**
	 * Logs access to equipment_component_type table from the save equipment
	 * component type REST endpoint.
	 *
	 * @param request - servlet request
	 * @param ecType - the equipmentComponentType with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.saveEquipmentComponentType(..)) && args(..,request)",
		            returning="ecType")
	public void saveEquipmentComponentTypeAccess(ServletWebRequest request, EquipmentComponentType ecType) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment_component_type",
			           ecType.getEquipmentComponentTypeId(),
			           generateJsonStringFromObject(ecType));
	}

	/**
	 * Logs access to equipment_component_type table from the remove equipment
	 * component type REST endpoint.
	 *
	 * @param request - servlet request
	 * @param id - the id for the equipment component type that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.EquipmentController.removeEquipmentComponentType(..)) && args(id,request)")
	public void removeEquipmentComponentTypeAccess(ServletWebRequest request, Integer id) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "equipment_component_type",
			           id,
			           "");
	}
	
	/* **********************
	 * Vehicle Controller
	 * **********************
	 */

	/**
	 * Logs all access to vehicle_class table. This advice covers all endpoints
	 * for the vehicle class, it only provides limited log information because
	 * the vehicle_class table is static it uses the standard DOT vehicle class
	 * definitions and should not be updated.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(VehicleController) && execution(* *VehicleClass(..)) && args(..,request)")
	public void vehicleClassAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "vehicle_class");
	}


	/**
	 * Handles access to the request for all vehicles. The
	 * AOP pointcut matches the REST endpoint readVehicle with a 
	 * single argument. 
	 *
	 * @param request - the httpRequest that drove this infromation request
	 * @param vehicleList - list of vehicleDTO returned by the target method
	 *
	 */
	@AfterReturning(pointcut="within(VehicleController) && execution(* readVehicle(*)) && args(request)", 
		            returning="vehicleList")
	public void allVehiclesAccess(ServletWebRequest request, Collection<VehicleDTO> vehicleList)
	{
		// get request user to determine access
		User user = getUserFromRequest(request);
		List<String> roles = user.getRoles();
		boolean redact = true;

		// if user has admin, training or vehicle roles then there is no redaction
		if(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_TRAINING") || roles.contains("ROLE_VEHICLE")) {redact = false;}

		if(redact)
		{
			for(VehicleDTO vehDTO : vehicleList)
			{
				vehDTO.setId(REDACT_TEXT);
				vehDTO.setVin(REDACT_TEXT);
			}
		}

		// write log record 
		writeLogRecord(user.getEmail(), 
			           request.getHttpMethod().toString(), 
			           "vehicle");
	}

	/**
	 * Handles access to the request for a single vehicle. The
	 * AOP pointcut matches the REST endpoint readVehicle that 
	 * returns a single VehicleDTO object. 
	 *
	 * @param request - the httpRequest that drove this infromation request
	 * @param vehDTO - the VehicleDTO returned by the target method
	 *
	 */
	@AfterReturning(pointcut="within(VehicleController) && execution(com.trihydro.cvpt.controller.dto.VehicleDTO readVehicle(..)) && args(..,request)",
					returning="vehDTO")
	public void readVehicleAccess(ServletWebRequest request, VehicleDTO vehDTO) 
	{
		// get request user to determine access
		User user = getUserFromRequest(request);
		List<String> roles = user.getRoles();
		boolean redact = true;

		// if user has admin, training or vehicle roles then there is no redaction
		if(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_TRAINING") || roles.contains("ROLE_VEHICLE")) {redact = false;}

		if(redact)
		{
			vehDTO.setId(REDACT_TEXT);
			vehDTO.setVin(REDACT_TEXT);
		}

		// write new log record
		writeLogRecord(user.getEmail(), request.getHttpMethod().toString(), "vehicle");
	}

	/**
	 * Logs access to vehicle table from the addVehicle REST endpoint.
	 *
	 * @param request - servlet request
	 * @param vehicleDTO - the vehicleDTO with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.VehicleController.addVehicle(..)) && args(vehicleDTO,request)",
		            returning="response")
	public void addVehicleAccess(ServletWebRequest request, VehicleDTO vehicleDTO, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "vehicle",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(vehicleDTO));
	}

	/**
	 * Logs access to vehicle table from the saveVehicle REST endpoint.
	 *
	 * @param request - servlet request
	 * @param vehicleDTO - the vehicleDTO with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.VehicleController.saveVehicle(..)) && args(..,request)",
		            returning="vehicleDTO")
	public void saveVehicleAccess(ServletWebRequest request, VehicleDTO vehicleDTO) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "vehicle",
			           vehicleDTO.getVehicleId(),
			           generateJsonStringFromObject(vehicleDTO));
	}

	/**
	 * Logs access to vehicle table from the removeVehicle REST endpoint.
	 *
	 * @param request - servlet request
	 * @param id - the id for the vehicle that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.VehicleController.removeVehicle(..)) && args(id,request)")
	public void removeVehicleAccess(ServletWebRequest request, Integer id) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "vehicle",
			           id,
			           "");
	}

	/**
	 * Logs access to equipment table when requesting available equipment
	 * for a vehicle.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(VehicleController) && execution(* readAvailableVehicleEquipment(..)) && args(..,request)")
	public void vehicleAvailableEquipmentAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "vehicle");
	}

	@Pointcut("execution(* com.trihydro.cvpt.controller.VehicleController.readAssociatedVehicleEquipment(..))" +
		" || execution(* com.trihydro.cvpt.controller.VehicleController.readVehicleEquipment(..))")
	private void readVehicleEquipmentPointcut() {}

	/**
	 * Logs all access to vehicle_equipment table from the read 
	 * VehicleEquipment REST endpoints.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("readVehicleEquipmentPointcut() && args(..,request)")
	public void readVehicleEquipmentAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "vehicle_equipment");
	}

	/**
	 * Logs access to vehicle_equipment table from the addVehicleEquipment
	 * REST endpoint
	 *
	 * @param request - servlet request
	 * @param veqDTO - the vehicleEquipmentDTO with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.VehicleController.addVehicleEquipment(..)) && args(..,veqDTO,request)",
		            returning="response")
	public void addVehicleEquipmentAccess(ServletWebRequest request, VehicleEquipmentDTO veqDTO, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "vehicle_equipment",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(veqDTO));
	}

	/**
	 * Logs access to vehicle_equipment table from the saveVehicleEquipment
	 * REST endpoint.
	 *
	 * @param request - servlet request
	 * @param veqDTO - the vehicleEquipmentDTO with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.VehicleController.saveVehicleEquipment(..)) && args(..,request)",
		            returning="veqDTO")
	public void saveVehicleEquipmentAccess(ServletWebRequest request, VehicleEquipmentDTO veqDTO) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "vehicle_equipment",
			           veqDTO.getVehicleEquipmentId(),
			           generateJsonStringFromObject(veqDTO));
	}

	/**
	 * Logs access to vehicle_equipment table from the removeVehicleEquipment
	 * REST endpoint.
	 *
	 * @param request - servlet request
	 * @param id - the id for the vehicleEquipment that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.VehicleController.removeVehicleEquipment(..)) && args(id,request)")
	public void removeVehicleEquipmentAccess(ServletWebRequest request, Integer id) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "vehicle_equipment",
			           id,
			           "");
	}


	/* **********************
	 * Training Controller
	 * **********************
	 */

	/**
	 * Logs access to training_type table from the read training type REST
	 * endpoints, all training types or a single one.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(TrainingController) && execution(* readTrainingType(..)) && args(..,request)")
	public void readTrainingTypeAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "training_type");
	}

	/**
	 * Logs access to training_type table from the addTrainingType REST endpoint.
	 *
	 * @param request - servlet request
	 * @param trainingType - the trainingType with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.TrainingController.addTrainingType(..)) && args(trainingType,request)",
		            returning="response")
	public void addTrainingTypeAccess(ServletWebRequest request, TrainingType trainingType, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "training_type",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(trainingType));
	}

	/**
	 * Logs access to training_type table from the saveTrainingType REST 
	 * endpoint.
	 *
	 * @param request - servlet request
	 * @param trainingType - the trainingType with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.TrainingController.saveTrainingType(..)) && args(..,request)",
		            returning="trainingType")
	public void saveTrainingTypeAccess(ServletWebRequest request, TrainingType trainingType) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "training_type",
			           trainingType.getTrainingTypeId(),
			           generateJsonStringFromObject(trainingType));
	}

	/**
	 * Logs access to training_type table from the removeTrainingType REST 
	 * endpoint.
	 *
	 * @param request - servlet request
	 * @param id - the id for the trainingType that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.TrainingController.removeTrainingType(..)) && args(id,request)")
	public void removeTrainingTypeAccess(ServletWebRequest request, Integer id) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "training_type",
			           id,
			           "");
	}

	/**
	 * Logs access to training table from the read training REST endpoints, all
	 * trainings or a single one.
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(TrainingController) && execution(* readTraining(..)) && args(..,request)")
	public void readTrainingAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "training");
	}

	/**
	 * Logs access to training table from the addTraining REST endpoint.
	 *
	 * @param request - servlet request
	 * @param trainingDTO - the trainingDTO with data entered by client
	 * @param response - the http response returned to the client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.TrainingController.addTraining(..)) && args(trainingDTO,request)",
		            returning="response")
	public void addTrainingAccess(ServletWebRequest request, TrainingDTO trainingDTO, ResponseEntity response) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "training",
			           getIdFromResponse(response),
			           generateJsonStringFromObject(trainingDTO));
	}

	/**
	 * Logs access to training table from the saveTraining REST endpoint.
	 *
	 * @param request - servlet request
	 * @param trainingDTO - the trainingDTO with updated data entered by client
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.TrainingController.saveTraining(..)) && args(..,request)",
		            returning="trainingDTO")
	public void saveTrainingAccess(ServletWebRequest request, TrainingDTO trainingDTO) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "training",
			           trainingDTO.getTrainingId(),
			           generateJsonStringFromObject(trainingDTO));
	}

	/**
	 * Logs access to training table from the removeTraining REST endpoint.
	 *
	 * @param request - servlet request
	 * @param id - the id for the training that was deleted
	 */
	@AfterReturning(pointcut="execution(* com.trihydro.cvpt.controller.TrainingController.removeTraining(..)) && args(id,request)")
	public void removeTrainingAccess(ServletWebRequest request, Integer id) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "training",
			           id,
			           "");
	}



	/* **********************
	 * Report Controller
	 * **********************
	 */

	/**
	 * Logs all access for vehicle organization reports
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(ReportController) && execution(* *VehicleOrganization(..)) && args(..,request)")
	public void vehicleOrganizationReportAccess(ServletWebRequest request) 
	{
		// write new log records
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "vehicle");
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "vehicle_class");
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "organization");
	}

	/**
	 * Logs all access for equipment deployed reports
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(ReportController) && execution(* *EquipmentDeployed(..)) && args(..,request)")
	public void equipmentDeployedReportAccess(ServletWebRequest request) 
	{
		// write new log records
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "equipment");
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "equipment_type");
	}

	/**
	 * Logs all access for participant training reports
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(ReportController) && execution(* *ParticipantTraining(..)) && args(..,request)")
	public void participantTrainingReportAccess(ServletWebRequest request) 
	{
		// write new log records
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "training");
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "training_type");
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "participant_training");
	}

	/* **********************
	 * User Controller
	 * **********************
	 */

	/**
	 * Logs all access for user roles
	 *
	 * @param request  servlet request
	 */
	@AfterReturning("within(UserController) && execution(* *Role(..)) && args(..,request)")
	public void userRoleAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "role");
	}

	/**
	 * Logs access to Auth0 user from the createUser REST endpoint
	 *
	 * @param request  servlet request
	 * @param the http response returned to the client
	 */
	@AfterReturning("execution(* com.trihydro.cvpt.controller.UserController.createUser(..)) && args(user,request)")
	public void addUserAccess(ServletWebRequest request, User user) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "auth0_user",
			           new Integer(0),
			           generateJsonStringFromObject(user));
	}

	/**
	 * Logs access to Auth0 user from the updateUser REST endpoint
	 *
	 * @param request - servlet request
	 * @param user - the user data being updated 
	 */
	@AfterReturning("execution(* com.trihydro.cvpt.controller.UserController.updateUser(..)) && args(..,user,request)")
	public void updateUserAccess(ServletWebRequest request, User user) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "auth0_user",
			           new Integer(0),
			           generateJsonStringFromObject(user));
	}


	/**
	 * Logs access to Auth0 user password from the updatePasswordForUser REST 
	 * endpoint
	 *
	 * @param request - servlet request
	 * @param user - the user data being updated 
	 */
	@AfterReturning("execution(* com.trihydro.cvpt.controller.UserController.updatePasswordForUser(..)) && args(userPassword,request)")
	public void updatePasswordForUserAccess(ServletWebRequest request, UserPassword userPassword) 
	{
		// just get the auth0 user id from userPassword object
		String auth0Id = "{\"user_id\":\"" + userPassword.getUserId() + "\"}";
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), 
			           request.getHttpMethod().toString(), 
			           "auth0_user_password",
			           new Integer(0),
			           auth0Id);
	}


	/**
	 * Logs access to Auth0 user from the readUser REST endpoint
	 *
	 * @param request - servlet request
	 */
	@AfterReturning("execution(* com.trihydro.cvpt.controller.UserController.readUser(..)) && args(request)")
	public void readUserAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord(getUserLoginFromRequest(request), request.getHttpMethod().toString(), "auth0_user");
	}


	@Pointcut("execution(* com.trihydro.cvpt.controller.UserController.authorizeUser(..))" + 
		" || execution(* com.trihydro.cvpt.controller.UserController.getEmailVerificationForUser(..))")
	private void loginUserPointcut() {}

	/**
	 * Logs all access to auth0 user for user login set up REST endpoints 
	 * authorize user and email verification for user.
	 *
	 * @param request - servlet request
	 */
	@AfterReturning("loginUserPointcut() && args(..,request)")
	public void loginUserAccess(ServletWebRequest request) 
	{
		// write new log record
		writeLogRecord("initial user authorization", request.getHttpMethod().toString(), "auth0_user");
	}


	/* **********************
	 * Utility Routines
	 * **********************
	 */

	/**
	 * Generates a Json representation of the given object.
	 *
	 * @param obj - the object to covert to Json
	 *
	 * @return a String json representation of the object
	 */
	private String generateJsonStringFromObject(Object obj)
	{
		JSONObject jsonObject = new JSONObject(obj);
		return jsonObject.toString();
	}


	/**
	 * Extracts the id integer from the header of an http response.
	 * <p>
	 * For all newly created objects, generated from a POST call,
	 * the response header contains a location url that includes
	 * the id of the newly created object at the end of the url.
	 * <p>
	 * The location url is of the form:
	 *   http://localhost:8080/cvpt/participant/872
	 * 
	 * @param response - the http response containing location in the header
	 * 
	 * @return an Integer id value for the new object
	 *
	 */
	private Integer getIdFromResponse(ResponseEntity response)
	{
		// get the new participant id from the response header
		String url = response.getHeaders().getFirst(HttpHeaders.LOCATION);

		// confirm there was a location url defined
		Integer id;
		if (url == null)
		{
			id = new Integer(0);
		} else {
			// match id at end of url
			Matcher matcher = idPattern.matcher(url);
			matcher.find();
			String idText = matcher.group();
			id = new Integer(Integer.parseInt(idText));
		}
		
		return id;
	}


	/**
	 * Extracts the user from the given servlet request. The 
	 * user is identified by the authorization token included
	 * in the request. The authorization token must be a valid
	 * Auth0 idToken for a user.
	 *
	 * @param request - the current http servlet request 
	 *
	 * @return a User identified in the servlet request
	 */
	private User getUserFromRequest(ServletWebRequest request)
	{
		// get id_token passed with the request could return null
		// if token is not in request header. No token appears in 
		// for software test calls that skip the servlet security. 
		String idToken = request.getHeader("authorization");
		User user;

		if(idToken != null)
		{
			Matcher matcher = pattern.matcher(idToken);
			// match leading "bearer" tag - discard it
			matcher.find();

			// match the idToken
			matcher.find();
			idToken = matcher.group();
			
			user = userService.getUserFromToken(idToken);
		} else {
			user = new User();

			// set default login id and roles for testing
			user.setEmail(userLogin);
			List<String> roles = new ArrayList<>();
			roles.add(userRole);
			user.setRoles(roles);
		}
		
		return user;
	}


	/**
	 * Extracts the user name from the given servlet request.
	 *
	 * @param request - the current http servlet request 
	 *
	 * @return a String the users login 
	 */
	private String getUserLoginFromRequest(ServletWebRequest request)
	{
		return getUserFromRequest(request).getEmail();
	}


	/**
	 * Writes the given data as a record in the database log table.
	 *
	 * @param loginId - the users login id from the current request
	 * @param httpMethod - the http method of the current request (e.g. GET, PUT, POST)
	 * @param table - name of the database table being accessed
	 */
	private void writeLogRecord(String loginId, String httpMethod, String table)
	{
		// write the log record ignore the returned record
		Log logRecord = logRepository.save(new Log(loginId, httpMethod, table));
	}

	/**
	 * Writes the given data as a record in the database log table.
	 *
	 * @param loginId - the users login id from the current request
	 * @param httpMethod - the http method of the current request (e.g. GET, PUT, POST)
	 * @param table - name of the database table being accessed
	 * @param recordId - integer id value for record
	 * @param recordData - json string with data values for record 
	 */
	private void writeLogRecord(String loginId, String httpMethod, String table, Integer recordId, String recordData)
	{
		// write the log record ignore the returned record
		Log logRecord = logRepository.save(new Log(loginId, httpMethod, table, recordId, recordData));
	}
}
