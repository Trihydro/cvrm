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

import com.trihydro.cvpt.model.Organization;
import com.trihydro.cvpt.model.Participant;
import com.trihydro.cvpt.model.ParticipantVehicle;
import com.trihydro.cvpt.model.ParticipantTraining;
import com.trihydro.cvpt.repository.OrganizationRepository;
import com.trihydro.cvpt.repository.ParticipantRepository;
import com.trihydro.cvpt.repository.ParticipantTrainingRepository;
import com.trihydro.cvpt.repository.ParticipantVehicleRepository;
import com.trihydro.cvpt.exceptions.RecordNotFoundException;

import java.util.List;


/**
 * A service to manage and describe participants and organizations.
 *
 *
 */
@Component
public class ParticipantService
{

	private final ParticipantRepository participantRepository;
	private final OrganizationRepository organizationRepository;
	private final ParticipantVehicleRepository participantVehicleRepository;
	private final ParticipantTrainingRepository participantTrainingRepository;
	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	ParticipantService(ParticipantRepository participantRepository,
					   OrganizationRepository organizationRepository,
					   ParticipantVehicleRepository participantVehicleRepository,
					   ParticipantTrainingRepository participantTrainingRepository) 
	{
		this.organizationRepository = organizationRepository;
		this.participantRepository = participantRepository;
		this.participantVehicleRepository = participantVehicleRepository;
		this.participantTrainingRepository = participantTrainingRepository;
	}

	/******* get methods *******/

	/**
	 * Returns the participant object identified by the given participant id.
	 *
	 * @param participantId - id to identify a specific participant record
	 *
	 * @return Participant associated with the given participantId
	 */
	public Participant getParticipant(Integer participantId) 
	{
		this.validateParticipant(participantId);
		return this.participantRepository.findOne(participantId);
	}

	/**
	 * Returns list of all participants in system
	 *
	 * @return a List of all participants
	 */
	public List<Participant> getAllParticipants() 
	{
		return this.participantRepository.findAll();
	}

	/**
	 * Returns list of all organizations in system
	 *
	 * @return a List of all organizations
	 */
	public List<Organization> getAllOrganizations() 
	{
		return this.organizationRepository.findAll();
	}

	/**
	 * Returns the organization object identified by the given 
	 * organization id.
	 *
	 * @param organizationId - id to identify a specific organization
	 *
	 * @return an organization instance matching the given id
	 */
	public Organization getOrganization(Integer organizationId) 
	{
		validateOrganization(organizationId);
		return organizationRepository.findOne(organizationId);
	}

	/**
	 * Returns list of all participant vehicles relations that are associated
	 * with the given participant
	 *
	 * @return a List of all ParticipantVehicle relations for the given participant
	 */
	public List<ParticipantVehicle> getParticipantVehicles(Integer participantId) 
	{
		return this.participantVehicleRepository.findByParticipantId(participantId);
	}

	/**
	 * Returns the participant vehicle relation object identified by the given 
	 * participant vehicle id.
	 *
	 * @param participantVehicleId - identifies a specific participant vehicle relation
	 *
	 * @return a ParticipantVehicle instance matching the given id
	 */
	public ParticipantVehicle getParticipantVehicle(Integer participantVehicleId) 
	{
		this.validateParticipantVehicle(participantVehicleId);
		return this.participantVehicleRepository.findOne(participantVehicleId);
	}

	/**
	 * Returns list of all participant training relations that are associated
	 * with the given participant
	 *
	 * @return a List of all ParticipantTraining relations for the given participant
	 */
	public List<ParticipantTraining> getParticipantTrainings(Integer participantId) 
	{
		return this.participantTrainingRepository.findByParticipantId(participantId);
	}

	/**
	 * Returns the participant training relation object identified by the given 
	 * participant training id.
	 *
	 * @param participantTrainingId - identifies a specific participant training relation
	 *
	 * @return a ParticipantTraining instance matching the given id
	 */
	public ParticipantTraining getParticipantTraining(Integer participantTrainingId) 
	{
		this.validateParticipantTraining(participantTrainingId);
		return this.participantTrainingRepository.findOne(participantTrainingId);
	}

	/******* add methods *******/

	/**
	 * Adds the given participant to the repository or updates the
	 * participant if the participantId is specified.
	 *
	 * @param participant to add/update to the repository
	 */
	public void addParticipant(Participant participant) 
	{
		// confirm participant exists before updating
		if (participant.participantIdExists())
		{
			validateParticipant(participant.getParticipantId());
		}
		
		participantRepository.save(participant);
	}

	/**
	 * Adds the given organization to the repository or updates
	 * the organization if the organizationId is specified.
	 *
	 * @param Organization to add/update to the repository
	 */
	public void addOrganization(Organization organization) 
	{
		// confirm organization exists before updating
		if (organization.organizationIdExists())
		{
			validateOrganization(organization.getOrganizationId());
		}

		this.organizationRepository.save(organization);
	}

	/**
	 * Adds the given participant vehicle to the repository or 
	 * updates the participant vehicle if the participantVehicleId
	 * is specified.
	 *
	 * @param participantVehicle - participant vehicle relation to add/update to the repository
	 */
	public void addParticipantVehicle(ParticipantVehicle participantVehicle)
	{
		this.participantVehicleRepository.save(participantVehicle);
	}

	/**
	 * Adds the given participant training to the repository or 
	 * updates the participant training if the participantTrainingId
	 * is specified.
	 *
	 * @param participantTraining - participant training relation to add/update to the repository
	 */
	public void addParticipantTraining(ParticipantTraining participantTraining)
	{
		this.participantTrainingRepository.save(participantTraining);
	}


	/******* delete methods *******/

	/**
	 * Deletes the given participant from the repository.
	 *
	 * @param participantId - identifies the participant to delete
	 */
	public void deleteParticipant(Integer participantId) 
	{
		this.validateParticipant(participantId);
		this.participantRepository.delete(participantId);
	}

	/**
	 * Deletes the given organization from the repository.
	 *
	 * @param organizationId - identifies the organization to delete
	 */
	public void deleteOrganization(Integer organizationId) 
	{
		this.validateOrganization(organizationId);
		this.organizationRepository.delete(organizationId);
	}

	/**
	 * Deletes the given participant vehicle relation from the repository.
	 *
	 * @param participantVehicleId - identifies the participant vehicle relation to delete
	 */
	public void deleteParticipantVehicle(Integer participantVehicleId) 
	{
		this.validateParticipantVehicle(participantVehicleId);
		this.participantVehicleRepository.delete(participantVehicleId);
	}

	/**
	 * Deletes the given participant training relation from the repository.
	 *
	 * @param participantTrainingId - identifies the participant training relation to delete
	 */
	public void deleteParticipantTraining(Integer participantTrainingId) 
	{
		this.validateParticipantTraining(participantTrainingId);
		this.participantTrainingRepository.delete(participantTrainingId);
	}


	/******* validate methods *******/

	/**
	 * Confirms that the requested participant is an existing one. 
	 * Throws a RecordNotFoundException if the given participant id
	 * does not match any participant records, but is silent if a 
	 * matching participant is found. 
	 *
	 * @param participantId - the participant id to validate
	 */
	private void validateParticipant(Integer participantId) 
	{
		this.participantRepository.findByParticipantId(participantId).orElseThrow(
			() -> new RecordNotFoundException("participant", 
											  "participant_id=" + participantId));
	}


	/**
	 * Confirms that the requested organization is an existing one.
	 * Throws a RecordNotFoundException if the given organization id
	 * does not match any organization records, but is silent if a 
	 * matching organization is found. 
	 *
	 * @param organizationId - the organization id to validate
	 */
	private void validateOrganization(Integer organizationId) 
	{
		this.organizationRepository.findByOrganizationId(organizationId).orElseThrow(
			() -> new RecordNotFoundException("organization", 
											  "organization_id=" + organizationId));
	}

	/**
	 * Confirms that the requested participant vehicle relation is an existing 
	 * one. Throws a RecordNotFoundException if the given participant vehicle id
	 * does not match any participant vehicle relation records, but is silent 
	 * if a matching participant vehicle relation is found. 
	 *
	 * @param participantVehicleId - the participant vehicle id to validate
	 */
	private void validateParticipantVehicle(Integer participantVehicleId)
	{
		this.participantVehicleRepository.findByParticipantVehicleId(participantVehicleId).orElseThrow(
			() -> new RecordNotFoundException("participant_vehicle",
											  "participant_vehicle_id=" + participantVehicleId));
	}

	/**
	 * Confirms that the requested participant training relation is an existing 
	 * one. Throws a RecordNotFoundException if the given participant training id
	 * does not match any participant training relation records, but is silent 
	 * if a matching participant training relation is found. 
	 *
	 * @param participantTrainingId - the participant training id to validate
	 */
	private void validateParticipantTraining(Integer participantTrainingId)
	{
		this.participantTrainingRepository.findByParticipantTrainingId(participantTrainingId).orElseThrow(
			() -> new RecordNotFoundException("participant_training",
											  "participant_training_id=" + participantTrainingId));
	}

}