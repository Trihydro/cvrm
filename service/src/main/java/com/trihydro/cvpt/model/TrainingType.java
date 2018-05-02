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


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "training_type")
public class TrainingType
{
	
	private Integer trainingTypeId;
	private String trainingType;

	/**
	 * Constructor - default for application context
	 * create new training type instance
	 */
	public TrainingType(String trainingType) 
	{
		this.trainingType = trainingType;
	}

	/**
	 * Constructor - default for application context
	 * update existing training type instance
	 */
	public TrainingType(Integer trainingTypeId,
		                String trainingType)
	{
		this.trainingTypeId = trainingTypeId;
		this.trainingType = trainingType;
	}

	/**
	 * Constructor - no arg for JPA
	 */
	TrainingType() {}

	// training type id
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getTrainingTypeId() 
	{
		return trainingTypeId;
	}

	public void setTrainingTypeId(Integer id)
	{
		this.trainingTypeId = id;
	}

	/**
	 * Checks if a training type id is defined
	 *
	 * @return True if id is defined otherwise False
	 */
	public boolean trainingTypeIdExists()
	{
		return (trainingTypeId != null);
	}

	// training type
	public String getTrainingType() 
	{
		return this.trainingType;
	}

	public void setTrainingType(String trainingType)
	{
		this.trainingType = trainingType;
	}
	
}