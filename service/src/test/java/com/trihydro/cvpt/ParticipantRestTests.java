package com.trihydro.cvpt;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = CvptApplication.class)
@WebAppConfiguration
public class ParticipantRestTests
{

	@Autowired
	private WebApplicationContext webContext;

	private MockMvc mockMvc;

	@Before
	public void setupMockMvc()
	{
		mockMvc = MockMvcBuilders
			.webAppContextSetup(this.webContext)
			.build();
	}


	@Test
	public void testOrganization() throws Exception
	{
		// test the group get 
		ResultActions result = mockMvc.perform(get("/cvpt/organizations"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first organizationId value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String id = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"organizationId",
										"Test database missing a record for organization. At least one organization must be defined");

		// test the single record get
		result = mockMvc.perform(get("/cvpt/organizations/" + id))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create a new organization
		String bodyJson = "{\"name\":\"Test POST organization succeeded\", \"isTruckingCompany\":\"N\"}";
		result = mockMvc.perform(post("/cvpt/organizations")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// get id for new organization included in location url
		String location = result.andReturn().getResponse().getHeader("Location");
		id = TestUtilityParse.getIdFromHeaderLocation(location, 
									"Incorrect location URL created from POST organization. Location: " + location);

		// create update for organization
		bodyJson = "{\"name\":\"Test PUT organization succeeded\", \"isTruckingCompany\":\"N\"}";
		result = mockMvc.perform(put("/cvpt/organizations/" + id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(bodyJson));

		// validate put succeeded
		result.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// delete the newly created organization
		// confirm delete succeeded 
		mockMvc.perform(delete(location))
			.andExpect(status().is(204));
	}

	
	@Test
	public void testParticipant() throws Exception
	{
		// get a known value for organization id
		ResultActions result = mockMvc.perform(get("/cvpt/organizations"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first organizationId value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String orgId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"organizationId",
										"Test database missing a record for organization. At least one organization must be defined");

		// create a new participant
		// use the known organizationId just extracted
		String bodyJson = "{\"firstName\":\"Test POST participant Succeded\",\"lastName\":\"Irvin\", \"organizationId\":" + orgId + ", \"start_date\":\"2017-04-01\"}";
		result = mockMvc.perform(post("/cvpt/participants")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// get id for new participant included in location url
		String location = result.andReturn().getResponse().getHeader("Location");
		String id = TestUtilityParse.getIdFromHeaderLocation(location, 
											"Incorrect location URL created from POST participant. Location: " + location);

		// test the group get
		mockMvc.perform(get("/cvpt/participants"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// test the single record get
		mockMvc.perform(get("/cvpt/participants/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create update for participant
		bodyJson = "{\"firstName\":\"Test PUT participant Succeded\",\"lastName\":\"Irvin\", \"organizationId\":" + orgId + "}";
		result = mockMvc.perform(put("/cvpt/participants/" + id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(bodyJson));

		// validate put succeeded
		result.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// delete the newly created participant 
		// confirm delete succeeded 
		mockMvc.perform(delete(location))
			.andExpect(status().is(204));
	}

	@Test
	public void testParticipantVehicle() throws Exception
	{
		// get a known value for vehicle id
		ResultActions result = mockMvc.perform(get("/cvpt/vehicles"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first vehicleId value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String vehId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"vehicleId",
										"Test database missing a record for vehicle. At least one vehicle must be defined");

		// get a known value for participant id
		result = mockMvc.perform(get("/cvpt/participants"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first participantId value from JSON content
		responseContent = result.andReturn().getResponse().getContentAsString();
		String parId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"participantId",
										"Test database missing a record for participant. At least one participant must be defined");

		// create a new participant vehicle
		// use the known vehicleId and participantId just extracted
		String bodyJson = "{\"vehicleId\":" + vehId + ", \"participantId\":" + parId + ", \"isPrimary\":\"N\"}";
		result = mockMvc.perform(post("/cvpt/participants/" + parId + "/vehicles")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// get id for new participant vehicle included in location url
		String location = result.andReturn().getResponse().getHeader("Location");
		String id = TestUtilityParse.getIdFromHeaderLocation(location, 
											"Incorrect location URL created from POST participant vehicle. Location: " + location);
		
		// test the group get
		mockMvc.perform(get("/cvpt/participants/" + parId + "/vehicles"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// test the single record get
		mockMvc.perform(get("/cvpt/participants/vehicles/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create update for participant vehicle
		bodyJson = "{\"vehicleId\":" + vehId + ", \"participantId\":" + parId + ", \"isPrimary\":\"Y\"}";
		result = mockMvc.perform(put("/cvpt/participants/vehicles/" + id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(bodyJson));

		// validate put succeeded
		result.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// delete the newly created equipment 
		// confirm delete succeeded 
		mockMvc.perform(delete(location))
			.andExpect(status().is(204));
	}


	@Test
	public void testParticipantTraining() throws Exception
	{
		// get a known value for training id
		ResultActions result = mockMvc.perform(get("/cvpt/trainings"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first trainId value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String trnId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"trainingId",
										"Test database missing a record for training. At least one training must be defined");

		// get a known value for participant id
		result = mockMvc.perform(get("/cvpt/participants"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first participantId value from JSON content
		responseContent = result.andReturn().getResponse().getContentAsString();
		String parId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"participantId",
										"Test database missing a record for participant. At least one participant must be defined");

		// create a new participant training
		// use the known trainingId and participantId just extracted
		String bodyJson = "{\"trainingId\":" + trnId + ", \"participantId\":" + parId + "}";
		result = mockMvc.perform(post("/cvpt/participants/" + parId + "/trainings")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// get id for new participant training included in location url
		String location = result.andReturn().getResponse().getHeader("Location");
		String id = TestUtilityParse.getIdFromHeaderLocation(location, 
											"Incorrect location URL created from POST participant training. Location: " + location);
		
		// test the group get
		mockMvc.perform(get("/cvpt/participants/" + parId + "/trainings"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// test the single record get
		mockMvc.perform(get("/cvpt/participants/trainings/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create update for participant training
		bodyJson = "{\"trainingId\":" + trnId + ", \"participantId\":" + parId + ", \"timeToComplete\":125, \"dateCompleted\":\"2016-02-08\"}";
		result = mockMvc.perform(put("/cvpt/participants/trainings/" + id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(bodyJson));

		// validate put succeeded
		result.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// delete the newly created equipment 
		// confirm delete succeeded 
		mockMvc.perform(delete(location))
			.andExpect(status().is(204));
	}

}