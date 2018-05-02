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

 package com.trihydro.cvpt.model.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trihydro.cvpt.exceptions.ServiceAccessException;
import com.trihydro.cvpt.exceptions.RecordNotFoundException;
import com.trihydro.cvpt.model.Role;
import com.trihydro.cvpt.model.User;
import com.trihydro.cvpt.model.UserPassword;
import com.trihydro.cvpt.model.UserTicket;
import com.trihydro.cvpt.model.AuthorizeToken;
import com.trihydro.cvpt.repository.RoleRepository;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.nio.charset.StandardCharsets;


/**
 * A service to manage system users and roles.
 *
 *
 */
@Component
public class UserService
{

	private final RoleRepository roleRepository;
	private Instant tokenExpireTime;
	private String accessToken;
	private final String domain;
	private final String connection;
	private final String apiClientId;
	private final String clientId;
	private final String apiClientSecret;
	private final String clientSecret;
	private final int    evtLife;	//email verification ticket life (sec)
	
	/**
	 * Constructor - application context
	 */	
	@Autowired
	UserService(RoleRepository roleRepository, Environment env) 
	{
		this.roleRepository = roleRepository;
		this.accessToken = "";
		this.tokenExpireTime = Instant.now();
		this.domain = env.getProperty("auth0.domain");
		this.apiClientId = env.getProperty("auth0.apiClientId");
		this.clientId = env.getProperty("auth0.clientId");
		this.clientSecret = env.getProperty("auth0.clientSecret");
		this.apiClientSecret = env.getProperty("auth0.apiClientSecret");
		this.connection = "Username-Password-Authentication";  //move to auth0.properties
		this.evtLife = 86400;  //move to auth0.properties
	}

	// get role

	/**
	 * Returns list of all roles defined in system
	 *
	 * @return a List of all roles
	 */
	public List<Role> getAllRoles() 
	{
		return this.roleRepository.findAll();
	}


	// get users 

	/**
	 * Returns list of user profiles for all users in system. The list of users
	 * and their credentials are maintained by the Auth0 service. 
	 *
	 * The string returned by the Auth0 service is a Json string containing a 
	 * set of user profiles. Most of the key value pairs in this string are of 
	 * no interest to this service.
	 *
	 * The key values that are of interest for each user in this set of user 
	 * profiles are:
	 * [
	 *   {
	 *     "email":"rsmith@gmail.com",
	 *     "email_verified": true,
	 *     "user_id": "auth0|58a71c25c7b75a07750e996b",
	 *     "user_metadata":
	 *          {
	 *             "organization": "Wyoming DOT"
	 *          },
	 *     "app_metadata":
	 *			{
	 *             "roles":
	 *                ["ROLE_TRAINING_MGR", "ROLE_EQUIPMENT_MGR"]
	 *          },
	 *     "given_name": "Rex",
	 *     "family_name": "Smith"
	 *   },
	 *   ...
	 * ]
	 *
	 * @return a List of profiles for all authorized users 
	 */
	public List<User> getAllUsers()
	{
		// get current access token for Auth0 api client with user access
		String currentAccessToken = getAccessToken();

		// build query 
		String authorization = "Bearer " + currentAccessToken;
		String queryUrl = "https://" + domain + "/api/v2/users";

		// make call to Auth0 service to get all users
		User user;
		List<User> userList = new ArrayList<>();
		HttpResponse<JsonNode> response;
		try {

			response = Unirest.get(queryUrl)
		    	.header("content-type", "application/json")
		    	.header("authorization", authorization)
		    	.asJson();

		    // iterate over all userProfiles from Auth0 and create users
		    JSONArray userProfiles = response.getBody().getArray();

		    for(int i=0; i < userProfiles.length(); i++){
		    	user = new User();
		    	populateUser(user, userProfiles.getJSONObject(i));
		    	userList.add(user);
		    }

		} catch(UnirestException e) {
			// recast UnirestException to an unchecked ServiceAccessException
			// to minimize upstream required catch types
			throw new ServiceAccessException("Failed accessing Auth0 user data endpoint", e);
		} catch(JSONException e) {
			throw new ServiceAccessException("Failed parsing Auth0 user profile", e);
		}
		 
		return userList;
	}

	/**
	 * Returns the user related to the given Auth0 user id
	 * 
	 */
	@Deprecated
	public User getUser(String userId)
	{

		// get current access token for Auth0 api client with user access
		// build header with access token
		String currentAccessToken = getAccessToken();
		String authorization = "Bearer " + currentAccessToken;

		// build url to auth0 user information endpoint
		// include_fields parameter indicates to retrieve all user fields
		String queryUrl = "https://" + domain + "/api/v2/users/" + userId + "?include_fields=false";	

		// make call to Auth0 service to get user information and populate user
		User user = new User();
		HttpResponse<JsonNode> response;
		try {

			response = Unirest.get(queryUrl)
  				.header("content-type", "application/json")
  				.header("authorization", authorization)
  				.asJson();

  			// error if user does not exist in Auth0
  			if(response.getStatus() == HttpStatus.NOT_FOUND.value()){
  				throw new RecordNotFoundException("Auth0 user", userId);
  			}

  			JSONObject userProfile = response.getBody().getObject();

  			// populate user object from user profile JSONObject
  			populateUser(user, userProfile);
  			

		} catch(UnirestException e) {
			// recast these exceptions to unchecked ServiceAccessException
			// to minimize upstream required catch types
			throw new ServiceAccessException("Failed accessing Auth0 user information endpoint", e);
		} catch(JSONException e) {
  			throw new ServiceAccessException("Failed parsing Auth0 user profile", e);
  		}

  		return user;

	}

	/**
	 * Confirms that the given new user data is a valid Auth0 user.
	 * The only user data used for this verification process is the
	 * token field of the given user object. This token field is a
	 * special JWT encoded token generated for new users.
	 * 
	 * @param user - a user object, must have token field populated
	 *
	 * @return a User populated with all user data from Auth0
	 */
	public User verifyUser(User user)
	{
		// get current access token for Auth0 management api client 
		// build header with access token
		String currentAccessToken = getAccessToken();
		String authorization = "Bearer " + currentAccessToken;

		// decode token from user to get user id
		Claims claims = Jwts.parser()
			.setSigningKey(clientSecret.getBytes(StandardCharsets.UTF_8))	
			.parseClaimsJws(user.getToken()).getBody();
	
		String userId = claims.get("userId", String.class);

		// build url to auth0 user information endpoint
		// include_fields parameter indicates to retrieve all user fields
		String queryUrl = "https://" + domain + "/api/v2/users/" + userId + "?include_fields=false";	

		// make call to Auth0 service to get user information and populate user
		HttpResponse<JsonNode> response;
		try {

			response = Unirest.get(queryUrl)
  				.header("content-type", "application/json")
  				.header("authorization", authorization)
  				.asJson();

  			// error if user does not exist in Auth0
  			if(response.getStatus() == HttpStatus.NOT_FOUND.value()){
  				throw new RecordNotFoundException("Auth0 user", userId);
  			}

  			JSONObject userProfile = response.getBody().getObject();

  			// populate user object from user profile JSONObject
  			populateUser(user, userProfile);
  			

		} catch(UnirestException e) {
			// recast these exceptions to unchecked ServiceAccessException
			// to minimize upstream required catch types
			throw new ServiceAccessException("Failed accessing Auth0 user information endpoint", e);
		} catch(JSONException e) {
  			throw new ServiceAccessException("Failed parsing Auth0 user profile", e);
  		}

  		return user;
	}






	/**
	 * Returns the user related to the given Auth0 id_token.
	 * 
	 */
	public User getUserFromToken(String idToken)
	{
		// build url to auth0 token information endpoint
		String queryUrl = "https://" + domain + "/tokeninfo";
		// build Json format body query
		String queryBody = "{\"id_token\":\"" + idToken + "\"}";

		// make call to Auth0 service to get user information and populate user
		User user = new User();
		HttpResponse<JsonNode> response;
		try {

			response = Unirest.post(queryUrl)
  				.header("content-type", "application/json")
  				.body(queryBody)
  				.asJson();

  			JSONObject userProfile = response.getBody().getObject();

  			// populate user object from user profile JSONObject
  			populateUser(user, userProfile);
  			

		} catch(UnirestException e) {
			// recast these exceptions to unchecked ServiceAccessException
			// to minimize upstream required catch types
			throw new ServiceAccessException("Failed accessing Auth0 id_token user information endpoint", e);
		} catch(JSONException e) {
  			throw new ServiceAccessException("Failed parsing Auth0 user profile", e);
  		}

  		return user;

	}

	// create/update users

	/**
	 * Create an auth0 user with the given user data. A new user
	 * is created through an Auth0 call. A special token string is
	 * created for the new user to use for the first time login 
	 * process. The token is a JWT format 
	 * (See <a href="https://www.jsonwebtoken.io">jsonwebtoken.io</a>)
	 * encoded string.   
	 *
	 * @param user - contains user creation data (email, roles, organization, names)
	 *
	 * @return a User with all data updated including a new userId
	 */
	public User createUser(User user) 
	{
		// get current access token for Auth0 api client with user access
		String currentAccessToken = getAccessToken();

		// build url to user update endpoint
		String queryUrl = "https://" + domain + "/api/v2/users";

		// build authorization header
		String authorization = "Bearer " + currentAccessToken;

		// build a random password
		String password = UUID.randomUUID().toString();

		// build Json format body query
		JSONObject body = new JSONObject();
		body.put("connection", connection);
		body.put("email", user.getEmail());
		body.put("password", password);
		body.put("user_metadata", new JSONObject().put("organization", user.getOrganization()));
		body.put("email_verified", false);
		body.put("verify_email", false);
		body.put("app_metadata", new JSONObject().put("roles", user.getRoles()));
		body.put("given_name", user.getGivenName());
		body.put("family_name", user.getFamilyName());


		// convert to JSON format string
		String queryBody = body.toString();

		// make call to Auth0 service to create a new user
		HttpResponse<JsonNode> response;
		try {

			response = Unirest.post(queryUrl)
  				.header("content-type", "application/json")
  				.header("authorization",authorization)
  				.body(queryBody)
  				.asJson();

  			JSONObject userProfile = response.getBody().getObject();

  			// populate user object from new user profile JSONObject
  			populateUser(user, userProfile);

  			// add special JWT token for the new user
  			String jwtStr = Jwts.builder()
				.claim("email", user.getEmail())
				.claim("password", password)		
				.claim("userId", user.getUserId())
				.signWith(SignatureAlgorithm.HS256, clientSecret.getBytes(StandardCharsets.UTF_8))
				.compact();

			user.setToken(jwtStr);

		} catch(UnirestException e) {
			// recast these exceptions to unchecked ServiceAccessException
			// to minimize upstream required catch types
			throw new ServiceAccessException("Failed accessing Auth0 create user endpoint", e);
		} catch(JSONException e) {
  			throw new ServiceAccessException("Failed parsing Auth0 user profile", e);
  		}

  		//user.setPassword(password);
  		return user;

	}

	/**
	 * Gets an email verification ticket for a given auth0 user. The
	 * verification ticket provides a ticket URL to be embedded as a link in an 
	 * email, allowing the user to click the link from the email to verify 
	 * themselves as an authorized user.
	 *
	 * @param userTicket - contains the resultUrl for callback
	 *
	 * @return a UserTicket with the new ticketUrl defined
	 */
	public UserTicket getUserEmailVerificationTicket(UserTicket userTicket) 
	{
		// get current access token for Auth0 api client with user access
		String currentAccessToken = getAccessToken();

		// build url to user update endpoint
		String queryUrl = "https://" + domain + "/api/v2/tickets/email-verification";

		// build authorization header
		String authorization = "Bearer " + currentAccessToken;

		// build Json format body query 
		JSONObject body = new JSONObject();
		body.put("result_url", userTicket.getResultUrl());
		body.put("user_id", userTicket.getUserId());
		body.put("ttl_sec", evtLife);

		String queryBody = body.toString();

		// make call to Auth0 service to get email verification ticket for user
		HttpResponse<JsonNode> response;
		try {

			response = Unirest.post(queryUrl)
  				.header("content-type", "application/json")
  				.header("authorization",authorization)
  				.body(queryBody)
  				.asJson();

  			JSONObject ticket = response.getBody().getObject();

  			// populate user ticket from returned ticket JSONObject
  			userTicket.setTicketLife(evtLife);
  			userTicket.setTicketUrl(ticket.getString("ticket"));


		} catch(UnirestException e) {
			// recast these exceptions to unchecked ServiceAccessException
			// to minimize upstream required catch types
			throw new ServiceAccessException("Failed accessing Auth0 email verification ticket endpoint", e);
		} catch(JSONException e) {
  			throw new ServiceAccessException("Failed parsing Auth0 email verification ticket", e);
  		}

  		return userTicket;

	}

	/**
	 * Gets a password change ticket for a given auth0 user. The
	 * password change ticket provides a ticket URL that reroutes the user to an Auth0 password change prompt,
	 * allowing them to change their password. This call is used for allowing new users to change their password,
	 * as well as existing users. 
	 * @param userTicket - contains the resultUrl for callback
	 *
	 * @return a UserTicket with the new ticketUrl defined
	 */
	public UserTicket getPasswordChangeTicket(UserTicket userTicket) 
	{
		// get current access token for Auth0 api client with user access
		String currentAccessToken = getAccessToken();

		// build url to user update endpoint
		String queryUrl = "https://" + domain + "/api/v2/tickets/password-change";

		// build authorization header
		String authorization = "Bearer " + currentAccessToken;

		// build Json format body query 
		JSONObject body = new JSONObject();
		body.put("result_url", userTicket.getResultUrl());
		body.put("user_id", userTicket.getUserId());
		body.put("ttl_sec", evtLife);

		String queryBody = body.toString();

		// make call to Auth0 service to get password change ticket for user
		HttpResponse<JsonNode> response;
		try {

			response = Unirest.post(queryUrl)
  				.header("content-type", "application/json")
  				.header("authorization",authorization)
  				.body(queryBody)
  				.asJson();

  			JSONObject ticket = response.getBody().getObject();

  			// populate user ticket from returned ticket JSONObject
  			userTicket.setTicketLife(evtLife);
  			userTicket.setTicketUrl(ticket.getString("ticket"));


		} catch(UnirestException e) {
			// recast these exceptions to unchecked ServiceAccessException
			// to minimize upstream required catch types
			throw new ServiceAccessException("Failed accessing Auth0 change password ticket endpoint", e);
		} catch(JSONException e) {
  			throw new ServiceAccessException("Failed parsing Auth0 change password ticket", e);
  		}

  		return userTicket;

	}

	/**
	 * Generates a valid Auth0 user id_token for the given
	 * new user. This call supports authorization of a new user.
	 * The new user object contains a token field that holds a 
	 * special token string that is created for the new user to 
	 * use for the first time login process. The token is a JWT format 
	 * (See <a href="https://www.jsonwebtoken.io">jsonwebtoken.io</a>)
	 * encoded string.  
	 *
	 * @param User - the new user
	 *
	 * @return a UserTicket with the new ticketUrl defined
	 */
	public AuthorizeToken authorizeUser(User user) 
	{
		
		// this Auth0 endpoint is deprecated but is currently used
		// because it supports the HS256 encryption standard
		// the replacement Auth0 call only supports RS256 encryption

		// build url to auth0 token endpoint
		String queryUrl = "https://" + domain + "/oauth/ro";

		// extract the email and password from the user token
		Claims claims = Jwts.parser()
			.setSigningKey(clientSecret.getBytes(StandardCharsets.UTF_8))	
			.parseClaimsJws(user.getToken()).getBody();

		String email = claims.get("email", String.class);
		String password = claims.get("password", String.class);
	    
	    // build Json format body query 
		JSONObject body = new JSONObject();
		body.put("client_id", this.clientId);	
		body.put("username", email);
		body.put("password", password);
		body.put("scope", "openid roles");
		body.put("connection", this.connection);
		body.put("grant_type", "password");

		String queryBody = body.toString();

		// make call to Auth0 service to get access token and extract data
		AuthorizeToken authorizeToken = new AuthorizeToken();
		HttpResponse<JsonNode> response;
		try {

			response = Unirest.post(queryUrl)
  				.header("content-type", "application/json")
  				.body(queryBody)
  				.asJson();

  			JSONObject tokenData = response.getBody().getObject();

  			// extract the id token from JSONObject
  			authorizeToken.setIdToken(tokenData.getString("id_token"));

		} catch(UnirestException e) {
			// recast these exceptions to unchecked ServiceAccessException
			// to minimize upstream required catch types
			throw new ServiceAccessException("Failed accessing Auth0 access token endpoint", e);
		} catch(JSONException e) {
  			throw new ServiceAccessException("Failed parsing Auth0 access token JWT", e);
  		}
			
  		return authorizeToken;
	}

	/**
	 * Updates the auth0 user with the data specified in the user object. All 
	 * the values specified in the user object will be updated to the auth0
	 * user. The specified values will overwrite the existing values in the 
	 * Auth0 user record. The two boolean user values, emailVerified and 
	 * blocked, must be specified in the user object otherwise they will update 
	 * to the default value {@code false}. A valid Auth0 user id must be 
	 * included in the user object, but it is not updated into the Auth0 user
	 * record.
	 * <p>
	 * The Auth0 user values that may be updated are:
	 * <ul>
	 * <li>emailVerified (required)
	 * <li>roles[]
	 * <li>givenName  
	 * <li>familyName
	 * <li>organization
	 * <li>blocked (required)
	 * </ul>
	 *
	 * @param user - contains the update data for a specific auth0 user 
	 * 
	 * @return a User with updated data
	 */
	public User updateUser(User user) 
	{

		// get current access token for Auth0 api client with user access
		String currentAccessToken = getAccessToken();

		// build url to user update endpoint
		String queryUrl = "https://" + domain + "/api/v2/users/" + user.getUserId();

		// build authorization header
		String authorization = "Bearer " + currentAccessToken;
			
		// build Json format body query
		JSONObject body = new JSONObject();
		//if(user.givenNameExists()) {body.put("given_name", user.getGivenName());}
		//if(user.familyNameExists()) {body.put("family_name", user.getFamilyName());}
		if(user.organizationExists()) {body.put("user_metadata", new JSONObject().put("organization", user.getOrganization()));}
		if(user.rolesExists()) {body.put("app_metadata", new JSONObject().put("roles", user.getRoles()));}
		body.put("email_verified", user.getEmailVerified());
		body.put("blocked", user.getBlocked());
		body.put("connection", connection);  //connection required to write email_verified

		// convert to JSON format string
		String queryBody = body.toString();

		// make call to Auth0 service to upate user data
		HttpResponse<JsonNode> response;
		try {

			response = Unirest.patch(queryUrl)
  				.header("content-type", "application/json")
  				.header("authorization",authorization)
  				.body(queryBody)
  				.asJson();

  			JSONObject userProfile = response.getBody().getObject();


  			// populate user object from user profile JSONObject
  			populateUser(user, userProfile);

		} catch(UnirestException e) {
			// recast these exceptions to unchecked ServiceAccessException
			// to minimize upstream required catch types
			throw new ServiceAccessException("Failed accessing Auth0 access update user endpoint", e);
		} catch(JSONException e) {
  			throw new ServiceAccessException("Failed parsing Auth0 user profile", e);
  		}

  		return user;
		
	}


	/**
	 * Updates the given user with the specified password. 
	 * 
	 * @param a userPassword to update into the specified user
	 * 
	 */
	public void updateUserPassword(UserPassword userPassword) 
	{

		// get current access token for Auth0 api client with user access
		String currentAccessToken = getAccessToken();

		// build url to user update endpoint
		String queryUrl = "https://" + domain + "/api/v2/users/" + userPassword.getUserId();

		// build authorization header
		String authorization = "Bearer " + currentAccessToken;
			
		// build Json format body query
		// email_verified set to true with password update
		JSONObject body = new JSONObject();
		body.put("connection", connection);
		body.put("password", userPassword.getPassword());

		String queryBody = body.toString();

		// make call to Auth0 service to upate user data
		HttpResponse<JsonNode> response;
		try {

			response = Unirest.patch(queryUrl)
  				.header("content-type", "application/json")
  				.header("authorization",authorization)
  				.body(queryBody)
  				.asJson();

		} catch(UnirestException e) {
			// recast these exceptions to unchecked ServiceAccessException
			// to minimize upstream required catch types
			throw new ServiceAccessException("Failed accessing Auth0 update user password endpoint", e);
		} catch(JSONException e) {
  			throw new ServiceAccessException("Failed parsing user password from Json", e);
  		} 
		
	}

	// utility methods

	/**
	 * Retrives a current Auth0 access_token for user data. This access 
	 * token is generated by the Auth0 servers and is specific to the 
	 * predefined Auth0 client that has scopes to read and write user 
	 * data. Each access_token generated from Auth0 is valid for 24 hours.
	 *
	 * The Auth0 access_token is returned in JWT (JSON Web Token) format
	 * like this: 
	 * {
	 *    "access_token": "eyJ...Ggg", 
	 *    "expires_in": 86400,
	 *    "scope": "read:users_email update:users_app_metadata",
	 *    "token_type": "Bearer"
	 * }
	 *
	 * @return String Auth0 v2 access_token 
	 */
	private String getAccessToken()
	{
		//if current stored token is valid then use it
		if (Instant.now().isBefore(tokenExpireTime)) 
		{
			return accessToken;

		} else {   //retrive a new token from auth0

			// build url to auth0 token endpoint
			String queryUrl = "https://" + domain + "/oauth/token";
			// build Json format body query
			String queryBody = "{\"grant_type\":\"client_credentials\",\"client_id\": \"" + apiClientId + "\",\"client_secret\": \"" + apiClientSecret + "\",\"audience\": \"https://" + domain + "/api/v2/\"}";

			// make call to Auth0 service to get access token and extract data
			HttpResponse<JsonNode> response;
			try {

				response = Unirest.post(queryUrl)
  					.header("content-type", "application/json")
  					.body(queryBody)
  					.asJson();

  				JSONObject jwt = response.getBody().getObject();

  				// extract the access token and expiration from JSONObject
  				// cut expire time by 10 sec to ensure we don't use an expired token
  				accessToken = jwt.getString("access_token");
  				tokenExpireTime = Instant.now().plusSeconds(jwt.getLong("expires_in") - 10L);

			} catch(UnirestException e) {
				// recast these exceptions to unchecked ServiceAccessException
				// to minimize upstream required catch types
				throw new ServiceAccessException("Failed accessing Auth0 access token endpoint", e);
			} catch(JSONException e) {
  				throw new ServiceAccessException("Failed parsing Auth0 access token JWT", e);
  			}
			
  			return accessToken;
		}
	}

	/**
	 * Populates the given user object with data parsed from the given Auth0
	 * user profile.  
	 *
	 * The user profile returned by the Auth0 service is a Json string 
	 * containing data for a given user. Only some of the key value pairs in 
	 * this user profile string are of interest to this service.
	 *
	 * The key values that are of interest for each user are:
	 * [
	 *   {
	 *     "email":"rsmith@gmail.com",
	 *     "email_verified": true,
	 *     "user_id": "auth0|58a71c25c7b75a07750e996b",
	 *     "user_metadata":
	 *          {
	 *             "organization": "Wyoming DOT"
	 *          },
	 *     "app_metadata":
	 *			{
	 *             "roles":
	 *                ["ROLE_TRAINING_MGR", "ROLE_EQUIPMENT_MGR"]
	 *          },
	 *     "given_name": "Rex",
	 *     "family_name": "Smith",
	 *     "blocked": false
	 *   },
	 *   ...
	 * ]
	 *
	 * @param user - the user object to populate with data from profile
	 * @param userProfile - the Auth0 standard user profile data as a JSONObject
	 */
	private void populateUser(User user, JSONObject userProfile)
	{
		// check that the user_id exists in the userProfile otherwise 
		// there was no valid user
		String userId = userProfile.optString("user_id");  //returns "" if no user_id
		if (userId.equals("")){
			throw new RecordNotFoundException("Auth0 user", "unknown user id");
		}
		// extract and set the required fields for the user
		user.setUserId(userId);
		user.setEmail(userProfile.optString("email"));
		user.setEmailVerified(userProfile.optBoolean("email_verified"));
		user.setGivenName(userProfile.optString("given_name"));    //these may move from top level
		user.setFamilyName(userProfile.optString("family_name"));  //these may move from top level
		user.setBlocked(userProfile.optBoolean("blocked"));

		/* extract orgaization from user_metadata. Not every user profile will
		 * contain the user_metadata.
		 */
		JSONObject metadata = userProfile.optJSONObject("user_metadata");
		if(metadata != null){
			user.setOrganization(metadata.optString("organization"));
		}
		
		/* extract role values from app_metadata. Not every user profile will 
		 * contain the app_metadata. If the app_metadata is present then the 
		 * roles value is extracted as an Object list then converted to a 
		 * string list this roundabout extraction is necessary because the 
		 * org.json implementation of JSONArray does not have a method to 
		 * retrive a typed collection.
		 */
		metadata = userProfile.optJSONObject("app_metadata");
		if(metadata != null) {
			List<Object> profileList = metadata.getJSONArray("roles").toList();
			List<String> roleList = profileList.stream().map(Object::toString).collect(Collectors.toList());
			user.setRoles(roleList);
		}
		
	}

}