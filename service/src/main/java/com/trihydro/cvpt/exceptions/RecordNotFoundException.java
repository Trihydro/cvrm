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

 package com.trihydro.cvpt.exceptions;


/**
 * Provides standard execption for a failed record retrival typically 
 * due to a reference to a non-existing object. 
 * This is extended from RuntimeException to allow a single catch at the top
 * level where these exceptions are handled, typically in a controller.
 *
 */
public class RecordNotFoundException extends RuntimeException 
{

	/**
	 * Creates the exception. The entity name should indicate the type 
	 * of object that was to be retrived. The entity reference should 
	 * describe the full reference that was used to look up the record 
	 * (e.g. 'vehicleId=723').
	 *
	 * @param entityName - the type of object that was to be retrieved
	 * @param entityReference - concatenated string of key values used for this retrival
	 */
	public RecordNotFoundException(String entityName, String entityReference) 
	{
		super("Unable to find " + entityName + " using reference " + entityReference);
	}
}