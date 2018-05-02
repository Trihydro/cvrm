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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

@Entity
@Table(name="participant")
public class Participant 
{

	private Integer participantId;
	private String firstName;
	private String lastName;
	private String email;
	private Organization organization;
	private Date  startDate;
	private Date  endDate;

	/**
	 * Constructor - no arg for JPA
	 */
	public Participant() {}

	// participantId
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getParticipantId() {return participantId;}
	public void setParticipantId(Integer id) {this.participantId = id;}

	/**
	 * Checks if a participant id is defined
	 *
	 * @return True if id is defined otherwise False
	 */
	public boolean participantIdExists() {return (participantId != null);}

	// firstName
	public String getFirstName() {return this.firstName;}
	public void setFirstName(String firstName) {this.firstName = firstName;}

	// lastName
	public String getLastName() {return this.lastName;}
	public void setLastName(String lastName) {this.lastName = lastName;}

	// email
	public String getEmail() {return this.email;}
	public void setEmail(String email) {this.email = email;}

	// organization
	@ManyToOne
	@JoinColumn(name="organization_id")
	public Organization getOrganization() {return this.organization;}
	public void setOrganization(Organization org) {this.organization = org;}

	// startDate
	@Temporal(TemporalType.DATE)
	public Date getStartDate() {return this.startDate;}
	public void setStartDate(Date start) {this.startDate = start;}

	// endDate
	@Temporal(TemporalType.DATE)
	public Date getEndDate() {return this.endDate;}
	public void setEndDate(Date end) {this.endDate = end;}

}