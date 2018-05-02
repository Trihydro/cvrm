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

 package com.trihydro.cvpt.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Table;

import java.util.Date;


@Entity
@Table(name="participant_training")
public class ParticipantTraining
{

	private Integer  participantTrainingId;
	private Integer	 participantId;
	private Training training;
	private Integer	 timeToComplete;
	private Date	 dateCompleted;

	/**
	 * Constructor - no arg for JPA
	 */
	public ParticipantTraining() {}

	// participantTrainingId 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getParticipantTrainingId() 
	{
		return this.participantTrainingId;
	}

	public void setParticipantTrainingId(Integer id)
	{
		this.participantTrainingId = id;
	}

	// participantId
	public Integer getParticipantId() 
	{
		return this.participantId;
	}

	public void setParticipantId(Integer id)
	{
		this.participantId = id;
	}

	// training
	@ManyToOne
	@JoinColumn(name="training_id")
	public Training getTraining()
	{
		return this.training;
	}

	public void setTraining(Training training)
	{
		this.training = training;
	}

	// timeToComplete
	public Integer getTimeToComplete() 
	{
		return this.timeToComplete;
	}

	public void setTimeToComplete(Integer timeToComplete)
	{
		this.timeToComplete = timeToComplete;
	}

	// dateCompleted
	@Temporal(TemporalType.DATE)
	public Date getDateCompleted()
	{
		return this.dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted)
	{
		this.dateCompleted = dateCompleted;
	}

}