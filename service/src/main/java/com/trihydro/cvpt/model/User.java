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


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class User 
{

	private String  userId;  
	private String  email;
	private boolean emailVerified;
	private String  givenName;
	private String  familyName;
	private String  organization;
	private List<String> roles;
	private boolean blocked;
	private String	token;  // JWT encoded new user token
	private String  password;

	/**
	 * Constructor - no arg
	 */
	public User() {
		this.userId = "";
		this.email = "";
		this.emailVerified = false;
		this.givenName = "";
		this.familyName = "";
		this.organization = "";
		this.roles = new ArrayList<String>();
		this.blocked = false;
		this.token = "";
		this.password = "";
	}

	// userId
	public String getUserId() {return this.userId;}
	public void setUserId(String id) {this.userId = id;}

	// email
	public String getEmail() {return this.email;} 
	public void setEmail(String email) {this.email = email;}

	// emailVerified
	public boolean getEmailVerified() {return this.emailVerified;}
	public void setEmailVerified(boolean ev) {this.emailVerified = ev;}

	// givenName
	public String getGivenName() {return this.givenName;}
	public void setGivenName(String givenName) {this.givenName = givenName;}
	public boolean givenNameExists() {return (this.givenName.equals("")) ? false : true;}

	// familyName
	public String getFamilyName() {return this.familyName;}
	public void setFamilyName(String familyName) {this.familyName = familyName;}
	public boolean familyNameExists() {return (this.familyName.equals("")) ? false : true;}

	// organization
	public String getOrganization() {return this.organization;}
	public void setOrganization(String org) {this.organization = org;}
	public boolean organizationExists() {return (this.organization.equals("")) ? false : true;}

	// roles
	public List<String> getRoles() {return this.roles;}
	public void setRoles(Collection<String> roles) {this.roles = new ArrayList<String>(roles);}
	public boolean rolesExists() {return (this.roles.isEmpty()) ? false : true;}

	// blocked
	public boolean getBlocked() {return this.blocked;}
	public void setBlocked(boolean blocked) {this.blocked = blocked;}

	// token
	public String getToken() {return this.token;}
	public void setToken(String token) {this.token = token;}

	// password  TOTO remove
	public String getPassword() {return this.password;}
	public void setPassword(String password) {this.password = password;}

}