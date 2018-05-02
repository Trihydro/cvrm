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

import com.trihydro.cvpt.model.Training;
import com.trihydro.cvpt.model.TrainingType;
import com.trihydro.cvpt.repository.TrainingRepository;
import com.trihydro.cvpt.repository.TrainingTypeRepository;
import com.trihydro.cvpt.exceptions.RecordNotFoundException;

import java.util.List;


/**
 * A service to manage and describe training 
 * and training types.
 *
 *
 */
@Component
public class TrainingService
{

	private final TrainingRepository trainingRepository;
	private final TrainingTypeRepository trainingTypeRepository;	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	TrainingService(TrainingRepository trainingRepository,
				   TrainingTypeRepository trainingTypeRepository) 
	{
		this.trainingRepository = trainingRepository;
		this.trainingTypeRepository = trainingTypeRepository;
	}

	// get training

	/**
	 * Returns the training object identified by the given training id.
	 *
	 * @param trainingId - id to identify a specific training record
	 *
	 * @return Training associated with the given trainingId
	 */
	public Training getTraining(Integer trainingId) 
	{
		this.validateTraining(trainingId);
		return this.trainingRepository.findOne(trainingId);
	}

	/**
	 * Returns list of all training in system
	 *
	 * @return a List of all training
	 */
	public List<Training> getAllTrainings() 
	{
		return this.trainingRepository.findAll();
	}


	// get training type

	/**
	 * Returns the TrainingType object identified by the given 
	 * training type id.
	 *
	 * @param trainingTypeId - id identifies a specific training type
	 *
	 * @return a TrainingType instance matching the given id
	 */
	public TrainingType getTrainingType(Integer trainingTypeId) 
	{
		validateTrainingType(trainingTypeId);
		return trainingTypeRepository.findOne(trainingTypeId);
	}

	/**
	 * Returns list of all training types in system
	 *
	 * @return a List of all TrainingTypes
	 */
	public List<TrainingType> getAllTrainingTypes() 
	{
		return this.trainingTypeRepository.findAll();
	}

	// get available trainings for participant

	/**
	 * Returns a list of all trainings that are not currently assigned to 
	 * the specified participant. 
	 *
	 * @param  participantId - identifies a specific participant
	 *
	 * @return a List of trainings not currently assigned to participant
	 */
	public List<Training> getAvailableTrainingsForParticipant(Integer participantId) 
	{
		return this.trainingRepository.getAvailableTrainingsForParticipant(participantId);
	}

	// add training
	
	/**
	 * Adds the given training to the repository or updates the
	 * training if the trainingId is specified.
	 *
	 * @param training to add/update to the repository
	 */
	public void addTraining(Training training) 
	{
		// confirm training exists before updating
		if (training.trainingIdExists())
		{
			validateTraining(training.getTrainingId());
		}
		
		trainingRepository.save(training);
	}

	// add training type

	/**
	 * Adds the given trainingType to the repository or updates the
	 * trainingType if the trainingTypeId is specified.
	 *
	 * @param trainingType to add/update to the repository
	 */
	public void addTrainingType(TrainingType trainingType) 
	{
		// confirm training type exists before updating
		if (trainingType.trainingTypeIdExists())
		{
			validateTrainingType(trainingType.getTrainingTypeId());
		}
		
		trainingTypeRepository.save(trainingType);
	}

	// delete training

	/**
	 * Deletes the given training from the repository.
	 *
	 * @param trainingId - identifies the training to delete
	 */
	public void deleteTraining(Integer trainingId) 
	{
		this.validateTraining(trainingId);
		this.trainingRepository.delete(trainingId);
	}

	// delete training type

	/**
	 * Deletes the given training type from the repository.
	 *
	 * @param trainingTypeId - identifies the training type to delete
	 */
	public void deleteTrainingType(Integer trainingTypeId) 
	{
		this.validateTrainingType(trainingTypeId);
		this.trainingTypeRepository.delete(trainingTypeId);
	}


	/**
	 * Confirms that the requested training is an existing one. 
	 * Throws a RecordNotFoundException if the given training id
	 * does not match any training records, but is silent if a 
	 * matching training is found. 
	 *
	 * @param trainingId - the training id to validate
	 */
	private void validateTraining(Integer trainingId) 
	{
		this.trainingRepository.findByTrainingId(trainingId).orElseThrow(
			() -> new RecordNotFoundException("training", 
											  "training_id=" + trainingId));
	}

	/**
	 * Confirms that the requested training type is an existing one.
	 * Throws a RecordNotFoundException if the given training type id
	 * does not match any training type records, but is silent if a 
	 * matching training type is found. 
	 *
	 * @param trainingTypeId - the training type id to validate
	 */
	private void validateTrainingType(Integer trainingTypeId) 
	{
		this.trainingTypeRepository.findByTrainingTypeId(trainingTypeId).orElseThrow(
			() -> new RecordNotFoundException("training_type", 
											  "training_type_id=" + trainingTypeId));
	}

}