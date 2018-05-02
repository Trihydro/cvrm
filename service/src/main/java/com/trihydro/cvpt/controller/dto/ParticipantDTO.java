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


import java.text.SimpleDateFormat;

import com.trihydro.cvpt.model.Participant;

/**
 * Container object to transfer participant data between 
 * web client and server.
 *
 */
public class ParticipantDTO 
{

	private Integer participantId;
	private String firstName;
	private String lastName;
	private Integer organizationId;
	private String organizationName;
	private String email;
	private String startDate;
	private String endDate;  


	public ParticipantDTO() {}

	public ParticipantDTO(Participant participant) {
		this.participantId = participant.getParticipantId();
		this.firstName = participant.getFirstName();
		this.lastName = participant.getLastName();
		this.organizationId = participant.getOrganization().getOrganizationId();
		this.organizationName = participant.getOrganization().getName();
		this.email = participant.getEmail();

		// only format the start date if it is not null
		if (participant.getStartDate() == null)
		{
			this.startDate = null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			this.startDate = df.format(participant.getStartDate());
		}

		// only format the end date if it is not null
		if (participant.getEndDate() == null)
		{
			this.endDate = null;
		} else {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			this.endDate = df.format(participant.getEndDate());
		}

	}

	/**
	 * Check if the participantId value exists. New participant
	 * objects will not have a participantId, participants to 
	 * update will have a participantId.
	 *
	 * @return True if an participantId value exists otherwise False
	 */
	public boolean participantIdExists() {return (participantId != null);}

	// participantId
	public Integer getParticipantId() {return participantId;}
	public void setParticipantId(Integer id) {this.participantId = id;}

	// firstName
	public String getFirstName() {return firstName;}
	public void	setFirstName(String name) {this.firstName = name;}

	// lastName
	public String getLastName() {return lastName;}
	public void setLastName(String name) {this.lastName = name;}

	// organizationId
	public Integer getOrganizationId() {return organizationId;}

	// organizationName
	public String getOrganizationName() {return organizationName;}

	// email
	public String getEmail() {return email;}
	public void setEmail(String email) {this.email = email;}

	// startDate
	public String getStartDate() {return startDate;}
	public void setStartDate(String dateText) {this.startDate = dateText;}

	// endDate
	public String getEndDate() {return endDate;}
	public void setEndDate(String dateText) {this.endDate = dateText;}

}