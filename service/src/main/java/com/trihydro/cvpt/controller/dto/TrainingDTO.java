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


import com.trihydro.cvpt.model.Training;


/**
 * Container object to transfer training data between 
 * web client and server.
 *
 */
public class TrainingDTO 
{

	private Integer trainingId;
	private String  training;
	private String	courseId;
	private Integer trainingTypeId;
	private String	trainingType;
	private String	notes;

	public TrainingDTO() {}

	public TrainingDTO(Training t) 
	{
		this.trainingId = t.getTrainingId();
		this.training = t.getTraining();
		this.courseId = t.getCourseId();
		this.trainingTypeId = t.getTrainingType().getTrainingTypeId();
		this.trainingType = t.getTrainingType().getTrainingType();
		this.notes = t.getNotes();
	}

	/**
	 * Check if the trainingId value exists. New training
	 * objects will not have a trainingId, trainings to 
	 * update will have an trainingId.
	 *
	 * @return True if an trainingId value exists otherwise False
	 */
	public boolean trainingIdExists()
	{
		return (trainingId != null);
	}

	public Integer getTrainingId() 
	{
		return trainingId;
	}

	public void setTrainingId(Integer id)
	{
		this.trainingId = id;
	}

	public String getTraining()
	{
		return training;
	}

	public String getCourseId() 
	{
		return courseId;
	}

	public Integer getTrainingTypeId()
	{
		return trainingTypeId;
	}

	public String getTrainingType()
	{
		return trainingType;
	}

	public String getNotes()
	{
		return notes;
	}

}