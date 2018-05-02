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


import com.trihydro.cvpt.model.ParticipantVehicle;

/**
 * Container object to transfer participant vehicle data between 
 * web client and server.
 *
 */
public class ParticipantVehicleDTO 
{

	private Integer participantVehicleId;
	private Integer participantId;
	private Integer vehicleId;
	private String  id;   //id from vehicle
	private String  isPrimary; 

	public ParticipantVehicleDTO() {}

	public ParticipantVehicleDTO(ParticipantVehicle participantVehicle) {
		this.participantVehicleId = participantVehicle.getParticipantVehicleId();
		this.participantId = participantVehicle.getParticipantId();
		this.vehicleId = participantVehicle.getVehicle().getVehicleId();
		this.id = participantVehicle.getVehicle().getId();
		this.isPrimary = participantVehicle.getIsPrimary();
	}

	/**
	 * Check if the participantVehicleId value exists. New participant
	 * vehicle relation objects will not have a participantVehicleId, 
	 * participant vehicle relations to update will have a participantVehicleId.
	 *
	 * @return True if a participantVehicleId value exists otherwise False
	 */
	public boolean participantVehicleIdExists() {return (participantVehicleId != null);}

	public Integer getParticipantVehicleId() {return participantVehicleId;}
	public void setParticipantVehicleId(Integer id) {this.participantVehicleId = id;}

	// participantId
	public Integer getParticipantId() {return participantId;}
	public void setParticipantId(Integer id) {this.participantId = id;}

	// vehicleId
	public Integer getVehicleId() {return vehicleId;}

	// id  - from the associated vehicle
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}

	// isPrimary
	public String getIsPrimary() {return isPrimary;}

}