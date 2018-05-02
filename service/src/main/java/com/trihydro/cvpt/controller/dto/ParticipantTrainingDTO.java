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

 package com.trihydro.cvpt.controller.dto;


import com.trihydro.cvpt.model.ParticipantTraining;

import java.text.SimpleDateFormat;

/**
 * Container object to transfer participant training data between 
 * web client and server.
 *
 */
public class ParticipantTrainingDTO 
{

	private Integer participantTrainingId;
	private Integer participantId;
	private Integer trainingId;
	private String  training;   //training name
	private Integer timeToComplete; 
	private String	dateCompleted;

	public ParticipantTrainingDTO() {}

	public ParticipantTrainingDTO(ParticipantTraining participantTraining) {
		this.participantTrainingId = participantTraining.getParticipantTrainingId();
		this.participantId = participantTraining.getParticipantId();
		this.trainingId = participantTraining.getTraining().getTrainingId();
		this.training = participantTraining.getTraining().getTraining();
		this.timeToComplete = participantTraining.getTimeToComplete();

		// only format the date if it is not null
		if (participantTraining.getDateCompleted() == null)
		{
			this.dateCompleted = null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			this.dateCompleted = df.format(participantTraining.getDateCompleted());
		}

	}

	/**
	 * Check if the participantTrainingId value exists. New participant
	 * training relation objects will not have a participantTrainingId, 
	 * participant training relations to update will have a participantTrainingId.
	 *
	 * @return True if a participantTrainingId value exists otherwise False
	 */
	public boolean participantTrainingIdExists()
	{
		return (participantTrainingId != null);
	}

	public Integer getParticipantTrainingId() 
	{
		return participantTrainingId;
	}

	public void setParticipantTrainingId(Integer id)
	{
		this.participantTrainingId = id;
	}

	public Integer getParticipantId()
	{
		return participantId;
	}

	public void setParticipantId(Integer id)
	{
		this.participantId = id;
	}

	public Integer getTrainingId()
	{
		return trainingId;
	}

	public String getTraining()
	{
		return training;
	}

	public Integer getTimeToComplete()
	{
		return timeToComplete;
	}

	public String getDateCompleted()
	{
		return dateCompleted;
	}

}