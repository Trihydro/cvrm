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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="training")
public class Training 
{

	
	private Integer trainingId;
	private String	training;
	private String	courseId;
	private TrainingType trainingType;
	private String notes;


	/**
	 * Constructor - no arg for JPA
	 */
	public Training() {}

	// trainingId 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getTrainingId() {
		return this.trainingId;
	}

	public void setTrainingId(Integer id)
	{
		this.trainingId = id;
	}

	/**
	 * Checks if a training id is defined
	 *
	 * @return True if id is defined otherwise False
	 */
	public boolean trainingIdExists()
	{
		return (trainingId != null);
	}

	// training
	public String getTraining()
	{
		return this.training;
	}

	public void setTraining(String training)
	{
		this.training = training;
	}

	// courseId
	public String getCourseId() {
		return this.courseId;
	}

	public void setCourseId(String id)
	{
		this.courseId = id;
	}

	// trainingType
	@ManyToOne
	@JoinColumn(name="training_type_id")
	public TrainingType getTrainingType() {
		return this.trainingType;
	}

	public void setTrainingType(TrainingType trainingType)
	{
		this.trainingType = trainingType;
	}

	// notes
	public String getNotes() 
	{
		return this.notes;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

}