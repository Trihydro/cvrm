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
 * Provides standard execption for a failed access to an external service.
 * This is extended from RuntimeException to allow a single catch at the top
 * level where these exceptions are handled, typically in a controller.
 *
 */
public class ServiceAccessException extends RuntimeException 
{

    /**
     * Exception constructor. Creates a ServiceAccessException with the 
     * specified message.
     *
     * @param msg - description of the service access fault
     */
    public ServiceAccessException(String msg) 
    {
        super(msg);
    }

    /**
     * Exception constructor. Mirrors the existing Throwable constructor for 
     * chaining exceptions. 
     *
     * @param e - root cause exception
     */
    public ServiceAccessException(Throwable e)
    {
        super(e);
    }

    /**
     * Exception constructor. Mirrors the existing Throwable constructor for 
     * chaining exceptions. 
     *
     * @param msg - description of the service access fault
     * @param e - root cause exception
     */
    public ServiceAccessException(String msg, Throwable e)
    {
        super(msg, e);
    }
}