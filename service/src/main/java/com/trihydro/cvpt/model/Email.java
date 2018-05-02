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

public class Email
{
	
	private String to;
	private String from;
	private String subject;
	private String body;

	Email(){}

	public Email(String to, String from, String subject, String body) 
	{
		this.to = to;
		this.from = from;
		this.subject = subject;
		this.body = body;
	}
		
	public String getTo() 
	{
		return this.to;
	}

	public String getFrom() 
	{
		return this.from;
	}

	public String getSubject() 
	{
		return this.subject;
	}

	public String getBody() 
	{
		return this.body;
	}

	public void setTo(String to)
	{
		this.to = to;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public void setBody(String body)
	{
		this.body = body;
	}	
}