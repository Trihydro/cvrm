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


public class UserTicket
{

	private String  userId;  
	private String  resultUrl;
	private String  ticketUrl;
	private int		ticketLife;


	/**
	 * Constructor - no arg
	 */
	public UserTicket() {
		this.userId = "";
		this.resultUrl = "";
		this.ticketUrl = "";
		this.ticketLife = 0;
	}

	// userId
	public String getUserId() {return this.userId;}
	public void setUserId(String id) {this.userId = id;}

	// resultUrl
	public String getResultUrl() {return this.resultUrl;}
	public void setResultUrl(String url) {this.resultUrl = url;}

	// ticketUrl
	public String getTicketUrl() {return this.ticketUrl;}
	public void setTicketUrl(String url) {this.ticketUrl = url;}

	// ticketLife
	public int getTicketLife() {return this.ticketLife;}
	public void setTicketLife(int t) {this.ticketLife = t;}
}