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

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Date;

@Entity
@Table(name="log")
public class Log 
{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long logId;

	public String  	userLogin;
	public Date 	entryDate; // set on db insert
	public String 	action;  // 10 character max
	public String 	entity;
	private Integer	recordId; //id for record in entity
	private String	recordData; //json data 1000 character max

	/**
	 * Constructor without record data. Logs information from get 
	 * calls that may return multiple records, so the record data 
	 * is not logged.
	 * 
	 * @param userLogin - user login for authorized user 
	 * @param action - the http action executed (e.g. GET, PUT, POST)
	 * @param entity - the table being read/written to
	 */
	public Log(String userLogin,
			   String action, 
			   String entity) 
	{
		this.userLogin = userLogin;
		this.action = action;
		this.entity = entity;
		this.entryDate = new Date();
		this.recordId = null;
		this.recordData = null;
	}

	/**
	 * Constructor with record data. Logs information from put, 
	 * post and patch calls that apply to a single record, so
	 * the single record data is captured. 
	 * 
	 * @param userLogin - user login for authorized user 
	 * @param action - the http action executed (e.g. GET, PUT, POST)
	 * @param entity - the table being read/written to
	 * @param recordId - the id of the record in the given entity
	 * @param recordData - json text of the record being written 
	 *
	 */
	public Log(String userLogin,
			   String action, 
			   String entity,
			   Integer recordId,
			   String recordData) 
	{
		this.userLogin = userLogin;
		this.action = action;
		this.entity = entity;
		this.entryDate = new Date();
		this.recordId = recordId;
		this.recordData = recordData;
	}


	/**
	 * Constructor - no arg for JPA
	 */
	Log() {}

	// logId
	public Long getLogId() {return logId;}

	// userLogin
	public String getUserLogin() {return this.userLogin;}

	// action
	public String getAction() {return this.action;}

	// entity
	public String getEntity() {return this.entity;}

	// recordId
	public Integer getRecordId() {return this.recordId;}

	// recordData
	public String getRecordData() {return this.recordData;}

}