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
@Table(name="role")
public class Role 
{

	private Integer roleId;
	private String 	role;
	private String  roleDescription;

	/**
	 * Constructor - no arg for JPA
	 */
	Role() {}

	// roleId
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getRoleId() 
	{
		return this.roleId;
	}

	public void setRoleId(Integer id)
	{
		this.roleId = id;
	}

	// role
	public String getRole() 
	{
		return this.role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	// roleDescription
	public String getRoleDescription() 
	{
		return this.roleDescription;
	}

	public void setRoleDescription(String roleDescription)
	{
		this.roleDescription = roleDescription;
	}

}