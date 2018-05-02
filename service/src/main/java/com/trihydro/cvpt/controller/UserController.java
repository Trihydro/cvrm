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

 package com.trihydro.cvpt.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.lang.Deprecated;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

import com.trihydro.cvpt.exceptions.RecordNotFoundException;
import com.trihydro.cvpt.exceptions.ServiceAccessException;
import com.trihydro.cvpt.model.Role;
import com.trihydro.cvpt.model.User;
import com.trihydro.cvpt.model.UserPassword;
import com.trihydro.cvpt.model.UserTicket;
import com.trihydro.cvpt.model.AuthorizeToken;
import com.trihydro.cvpt.model.service.UserService;

import org.springframework.http.HttpHeaders;

import io.jsonwebtoken.*;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import io.jsonwebtoken.impl.crypto.MacProvider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;


@CrossOrigin
@RestController
@RequestMapping("/cvpt")
class UserController 
{
	private final UserService userService;
	private Key key;
	/**
	 * Constructor - application context
	 */	
	@Autowired
	UserController(UserService userService) 
	{
		this.userService = userService;
		this.key = MacProvider.generateKey();
	}


	/*
	 * Roles requests
	 *
	 */
	
	@RequestMapping(method = RequestMethod.GET, value = "/roles")
	Collection<Role> readRole(ServletWebRequest request) 
	{
		return this.userService.getAllRoles();
	}


	/*
	 * Users requests
	 *
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/users")
	Collection<User> readUser(ServletWebRequest request)
	{
		return userService.getAllUsers();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/users")
	User createUser(@RequestBody User userData,
				   	 ServletWebRequest request) 
	{
		return userService.createUser(userData);		
	}


	@RequestMapping(method = RequestMethod.POST, value = "/users/verify")
	User readUser(@RequestBody User user,
				   ServletWebRequest request) 
	{
		return userService.verifyUser(user);
	}	

	@RequestMapping(method = RequestMethod.POST, value = "/users/emailVerification")
	UserTicket getEmailVerificationForUser(@RequestBody UserTicket ticketInfo,
											ServletWebRequest request)
	{
		return userService.getUserEmailVerificationTicket(ticketInfo);
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/users/password")
	ResponseEntity<?> updatePasswordForUser(@RequestBody UserPassword userPassword,
											ServletWebRequest request)
	{
		userService.updateUserPassword(userPassword);

		return ResponseEntity.ok().build();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/users/passwordTicket")
	UserTicket getPasswordChangeTicket(@RequestBody UserTicket ticketInfo,
											ServletWebRequest request)
	{
		return userService.getPasswordChangeTicket(ticketInfo);		
	}

	@RequestMapping(method = RequestMethod.POST, value = "/users/newUserpasswordTicket")
	UserTicket getNewUserPasswordChangeTicket(@RequestBody UserTicket ticketInfo,
											ServletWebRequest request)
	{
		return userService.getPasswordChangeTicket(ticketInfo);		
	}

	@RequestMapping(method = RequestMethod.PATCH, value = "/users/{userId}")
	User updateUser(@PathVariable String userId,
				    @RequestBody User inputUser,
				    ServletWebRequest request) 
	{
		return userService.updateUser(inputUser);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/users/authorize")
	AuthorizeToken authorizeUser(@RequestBody User user,
				    			  ServletWebRequest request) 
	{
		return userService.authorizeUser(user);
	}
	

	/**
	 * Handles RecordNotFoundException thrown from any request
	 * mapping endpoints defined in this controller
	 *
	 * @param e - the thrown RecordNotFoundException
	 */
	@ExceptionHandler(RecordNotFoundException.class)
	ResponseEntity<?> recordNotFound(RecordNotFoundException e)
	{
		return ResponseEntity.notFound().header("Error", e.getMessage()).build();
	}

	/**
	 * Handles ServiceAccessException thrown from any request mapping endpoints 
	 * defined in this controller. ServiceAccessExceptions are generated when 
	 * access to external services fail for some reason. 
	 *
	 * @param e - the thrown ServiceAccessException
	 */
	@ExceptionHandler(ServiceAccessException.class)
	ResponseEntity<?> serviceAccessFailure(ServiceAccessException e)
	{
		return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).header("Error", e.getMessage()).build();
	}

}
