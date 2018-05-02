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


public class RecordCount
{
	private String  type;
	private int     count;

	/**
	 * Constructor 
	 */
	public RecordCount(String type, int count)
	{
		this.type = type;
		this.count = count;
	}

	// type
	public String getType() {return type;}

	public void setType(String type) {this.type = type;}

	// count
	public int getCount() {return this.count;}

	public void setCount(int count) {this.count = count;}
	
}