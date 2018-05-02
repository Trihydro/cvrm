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
public class TrainingRestTests
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
	public void testTrainingType() throws Exception
	{
		// test the group get 
		ResultActions result = mockMvc.perform(get("/cvpt/trainingtypes"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first trainingTypeId value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String id = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"trainingTypeId",
										"Test database missing a record for training_type. At least one training type must be defined");

		// test the single record get
		result = mockMvc.perform(get("/cvpt/trainingtypes/" + id))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create a new training type
		String bodyJson = "{\"trainingType\":\"Test POST Training Type Succeded\"}";
		result = mockMvc.perform(post("/cvpt/trainingtypes")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// extract the newly generated location url for this new training type
		String location = result.andReturn().getResponse().getHeader("Location");

		// get id for new training type included in location url
		id = TestUtilityParse.getIdFromHeaderLocation(location, 
									"Incorrect location URL created from POST training_type. Location: " + location);

		// create update for training type
		bodyJson = "{\"trainingType\":\"Test PUT TrainingType succeeded\"}";
		result = mockMvc.perform(put("/cvpt/trainingtypes/" + id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(bodyJson));

		// validate put succeeded
		result.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// delete the newly created training type
		// confirm delete succeeded 
		mockMvc.perform(delete(location))
			.andExpect(status().is(204));
	}

	
	@Test
	public void testTraining() throws Exception
	{
		// get a known value for training type id
		ResultActions result = mockMvc.perform(get("/cvpt/trainingtypes"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first trainingTypeId value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String typeId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"trainingTypeId",
										"Test database missing a record for training_type. At least one training_type must be defined");


		// create a new training
		// use the known trainingTypeId just extracted
		String bodyJson = "{\"training\":\"Test POST training Succeded\", \"courseId\":\"123A\", \"trainingTypeId\":" + typeId + ", \"notes\":\"some notes here\"}";
		result = mockMvc.perform(post("/cvpt/trainings")
							.contentType(MediaType.APPLICATION_JSON)
							.content(bodyJson));

		// validate post succeeded 
		result.andExpect(status().is(201));

		// extract the newly generated location url for this new training
		String location = result.andReturn().getResponse().getHeader("Location");

		// get id for new training included in location url
		String id = TestUtilityParse.getIdFromHeaderLocation(location, 
											"Incorrect location URL created from POST training. Location: " + location);

		// test the group get
		mockMvc.perform(get("/cvpt/trainings"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// test the single record get
		mockMvc.perform(get("/cvpt/trainings/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create update for training
		bodyJson = "{\"training\":\"Test PUT training Succeded\", \"courseId\":\"123A\", \"trainingTypeId\":" + typeId + ", \"notes\":\"some notes here\"}";
		result = mockMvc.perform(put("/cvpt/trainings/" + id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(bodyJson));

		// validate put succeeded
		result.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// delete the newly created training
		// confirm delete succeeded 
		mockMvc.perform(delete(location))
			.andExpect(status().is(204));
	}

}