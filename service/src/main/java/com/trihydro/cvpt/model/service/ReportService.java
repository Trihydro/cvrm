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

import com.trihydro.cvpt.model.RecordCount;
import com.trihydro.cvpt.model.RecordPercentCount;
import com.trihydro.cvpt.model.RecordVehicleOrganization;
import com.trihydro.cvpt.model.EquipmentType;
import com.trihydro.cvpt.model.TrainingType;
import com.trihydro.cvpt.repository.EquipmentRepository;
import com.trihydro.cvpt.repository.EquipmentTypeRepository;
import com.trihydro.cvpt.repository.ParticipantRepository;
import com.trihydro.cvpt.repository.RecordVehicleOrganizationRepository;
import com.trihydro.cvpt.repository.TrainingRepository;
import com.trihydro.cvpt.repository.TrainingTypeRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;


/**
 * A service to generate report objects to support dashboard or
 * other application data. 
 *
 *
 */
@Component
public class ReportService
{

	private final RecordVehicleOrganizationRepository recVehicleOrganizationRepository;
	private final EquipmentRepository equipmentRepository;
	private final EquipmentTypeRepository equipmentTypeRepository;
	private final ParticipantRepository participantRepository;
	private final TrainingTypeRepository trainingTypeRepository;
	private final TrainingRepository trainingRepository;

	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	ReportService(RecordVehicleOrganizationRepository rvoRepository, 
				  EquipmentRepository equipmentRepository,
				  EquipmentTypeRepository equipmentTypeRepository,
				  ParticipantRepository participantRepository,
				  TrainingTypeRepository trainingTypeRepository,
				  TrainingRepository trainingRepository) 
	{
		this.recVehicleOrganizationRepository = rvoRepository;
		this.equipmentRepository = equipmentRepository;
		this.equipmentTypeRepository = equipmentTypeRepository;
		this.participantRepository = participantRepository;
		this.trainingTypeRepository = trainingTypeRepository;
		this.trainingRepository = trainingRepository;
	}

	/**
	 * Returns the vehicle organization records that count the number
	 * of vehicles for each vehicle class and organization combination.
	 *
	 * @return a List of vehicle organization records
	 */
	public List<RecordVehicleOrganization> getVehicleOrganizationRecords() 
	{
		return this.recVehicleOrganizationRepository.getVehicleOrganizationRecords();
	}

	/**
	 * Returns percent count records that hold values for percentage counts
	 * of deployed equipment types.
	 *
	 * @return a List of percent count records with deployed equipment data
	 */
	public List<RecordPercentCount> getEquipmentDeployedRecords() 
	{
		List<RecordPercentCount> recordList = new ArrayList<>();

		List<EquipmentType> equipmentTypes = this.equipmentTypeRepository.findAll();

		// get the equipment counts for each equipment type
		Optional<BigDecimal> optDecimal;
		int tc;
		int pc;
		for (EquipmentType equipmentType : equipmentTypes)
		{
			optDecimal = equipmentRepository.getCountOfEquipmentType(equipmentType.getEquipmentTypeId());
			tc = optDecimal.map(BigDecimal::intValue).orElse(0);

			optDecimal = equipmentRepository.getCountOfInstalledEquipmentType(equipmentType.getEquipmentTypeId());
			pc = optDecimal.map(BigDecimal::intValue).orElse(0);

			recordList.add(new RecordPercentCount(equipmentType.getEquipmentType(), tc, pc));

		}

		return recordList;
	}

	/**
	 * Returns count records that hold values for counts of various
	 * participant training queries.
	 *
	 * @return a List of count records with participant and training data
	 */
	public List<RecordCount> getParticipantTrainingRecords() 
	{
		List<RecordCount> recordList = new ArrayList<>();

		
		Optional<BigDecimal> optDecimal;
		int count;
		int total;

		// get total count of participants
		optDecimal = participantRepository.getCountOfParticipant();
		total = optDecimal.map(BigDecimal::intValue).orElse(0);
		recordList.add(new RecordCount("Total Participants", total));

		// 	get count of participants who have completed all of their training
		//  total - training completed = training pending
		optDecimal = participantRepository.getCountOfTrainingCompleteParticipant();
		count = optDecimal.map(BigDecimal::intValue).orElse(0);
		recordList.add(new RecordCount("Pending Training", total - count));

		// get counts for participants that have completed each training type
		// make type string: "Trained " + trainingType
		List<TrainingType> trainingTypes = this.trainingTypeRepository.findAll();
		for (TrainingType trainingType : trainingTypes)
		{
			optDecimal = trainingRepository.getCountOfCompletedTrainingType(trainingType.getTrainingTypeId());
			count = optDecimal.map(BigDecimal::intValue).orElse(0);

			recordList.add(new RecordCount("Trained " + trainingType.getTrainingType(), count));
		}

		// get average time for all participant training
		// training time is in minutes
		optDecimal = participantRepository.getAverageParticipantTrainingTime();
		count = optDecimal.map(BigDecimal::intValue).orElse(0);
		recordList.add(new RecordCount("Average Training Time", count));

		return recordList;
	}

}