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


@Entity
@Table(name = "organization")
public class Organization
{

	private Integer organizationId;
	public String name;
	public String isTruckingCompany; //enumerated value in db 'Y' or 'N'

	/**
	 * Constructor - default for application context
	 * create new organization instance
	 */
	public Organization(String name,
						String isTruckingCompany) 
	{
		this.name = name;
		this.isTruckingCompany = isTruckingCompany;
	}

	/**
	 * Constructor - default for application context
	 * update organization
	 */
	public Organization(Integer organizationId,
		                String name,
		                String isTruckingCompany)
	{
		this.organizationId = organizationId;
		this.name = name;
		this.isTruckingCompany = isTruckingCompany;
	}

	/**
	 * Constructor - no arg for JPA
	 */
	Organization() {}

	// organization id
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer id)
	{
		this.organizationId = id;
	}

	/**
	 * Checks if an organization id is defined
	 *
	 * @return True if id is defined otherwise False
	 */
	public boolean organizationIdExists()
	{
		return (organizationId != null);
	}

	// organization name

	public String getName() 
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	// is trucking company

	public String getIsTruckingCompany() 
	{
		return this.isTruckingCompany;
	}

	public void setIsTruckingCompany(String isTC)
	{
		this.isTruckingCompany = isTC;
	}
}