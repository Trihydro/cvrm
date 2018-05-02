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
import javax.persistence.Table;


@Entity
@Table(name="participant_vehicle")
public class ParticipantVehicle
{

	private Integer  participantVehicleId;
	private Integer	 participantId;
	private Vehicle  vehicle;
	private String   isPrimary; //enumerated value in db 'Y' or 'N'

	/**
	 * Constructor - no arg for JPA
	 */
	public ParticipantVehicle() {}

	// participantVehicleId 
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getParticipantVehicleId() 
	{
		return this.participantVehicleId;
	}

	public void setParticipantVehicleId(Integer id)
	{
		this.participantVehicleId = id;
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

	// vehicle
	@ManyToOne
	@JoinColumn(name="vehicle_id")
	public Vehicle getVehicle()
	{
		return this.vehicle;
	}

	public void setVehicle(Vehicle vehicle)
	{
		this.vehicle = vehicle;
	}

	// isPrimary
	public String getIsPrimary() 
	{
		return this.isPrimary;
	}

	public void setIsPrimary(String isPrimary)
	{
		this.isPrimary = isPrimary;
	}

}