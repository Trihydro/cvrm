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


public class RecordPercentCount
{
	private String  type;
	private int     totalCount;
	private int		percentCount;

	/**
	 * Constructor 
	 */
	public RecordPercentCount(String type, int totalCount, int percentCount)
	{
		this.type = type;
		this.totalCount = totalCount;
		this.percentCount = percentCount;
	}

	// type
	public String getType() {return type;}

	public void setType(String type) {this.type = type;}

	// total count
	public int getTotalCount() {return this.totalCount;}

	public void setTotalCount(int totalCount) {this.totalCount = totalCount;}

	// percent count
	public int getPercentCount() {return this.percentCount;}

	public void setPercentCount(int percentCount) {this.percentCount = percentCount;}
	
}