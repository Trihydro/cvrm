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
public class EquipmentRestTests
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
	public void testEquipmentTypes() throws Exception
	{
		// test the group get 
		ResultActions result = mockMvc.perform(get("/cvpt/equipmenttypes"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first equipmentType id value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String id = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"equipmentTypeId",
										"Test database missing a record for equipment_type. At least one equipment_type must be defined");

		// test the single record get
		result = mockMvc.perform(get("/cvpt/equipmenttypes/" + id))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create a new equipment type
		String bodyJson = "{\"equipmentType\":\"Test Post EquipmentType Succeeded\"}";
		result = mockMvc.perform(post("/cvpt/equipmenttypes")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// extract the newly generated location url for this new equipment type
		String location = result.andReturn().getResponse().getHeader("Location");

		// get id for new equipment type included in location url
		id = TestUtilityParse.getIdFromHeaderLocation(location, 
									"Incorrect location URL created from POST equipment_type. Location: " + location);

		System.out.println("****** equipment type id parsed from location header: " + id);

		// create update for equipment type
		bodyJson = "{\"equipmentType\":\"Test Put EquipmentType Succeeded\"}";
		result = mockMvc.perform(put("/cvpt/equipmenttypes/" + id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(bodyJson));

		// validate put succeeded
		result.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		System.out.println("****** just prior to delete equipment type location parsed from location header: " + location);

		// delete the newly created equipment type
		// confirm delete succeeded 
		mockMvc.perform(delete(location))
			.andExpect(status().is(204));
	}

	
	@Test
	public void testEquipment() throws Exception
	{
		// get a known value for equipment type id
		ResultActions result = mockMvc.perform(get("/cvpt/equipmenttypes"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first equipmentType id value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String typeId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"equipmentTypeId",
										"Test database missing a record for equipment_type. At least one equipment_type must be defined");

		// create a new equipment
		// use the known equipmentType id just extracted
		String bodyJson = "{\"description\":\"SN10763900A19\",\"assetId\":\"Test Post Equipment Succeeded\",\"dateInstalled\":\"2018-02-08\",\"equipmentTypeId\":" + typeId + "}";
		result = mockMvc.perform(post("/cvpt/equipment")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// extract the newly generated location url for this new equipment
		String location = result.andReturn().getResponse().getHeader("Location");

		// get id for new equipment included in location url
		String id = TestUtilityParse.getIdFromHeaderLocation(location, 
											"Incorrect location URL created from POST equipment. Location: " + location);

		// test the group get
		mockMvc.perform(get("/cvpt/equipment"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// test the single record get
		mockMvc.perform(get("/cvpt/equipment/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create update for equipment
		bodyJson = "{\"description\":\"SN10763900A19\",\"assetId\":\"Test Put Equipment Succeeded\",\"dateInstalled\":\"2018-02-08\",\"equipmentTypeId\":" + typeId + "}";
		result = mockMvc.perform(put("/cvpt/equipment/" + id)
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
	public void testEquipmentComponentTypes() throws Exception
	{
		// test the group get 
		ResultActions result = mockMvc.perform(get("/cvpt/equipment/componenttypes"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first equipmentComponentType id value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String id = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"equipmentComponentTypeId",
										"Test database missing a record for equipment_component_type. At least one equipment_component_type must be defined");

		// test the single record get
		result = mockMvc.perform(get("/cvpt/equipment/componenttypes/" + id))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create a new equipment component type
		String bodyJson = "{\"equipmentComponentType\":\"Test POST equipment component type succeeded\"}";
		result = mockMvc.perform(post("/cvpt/equipment/componenttypes")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// get id for new equipment component type included in location url
		String location = result.andReturn().getResponse().getHeader("Location");
		id = TestUtilityParse.getIdFromHeaderLocation(location, 
									"Incorrect location URL created from POST equipment_component_type. Location: " + location);

		// create update for equipment component type
		bodyJson = "{\"equipmentComponentType\":\"Test PUT equipment component type succeeded\"}";
		result = mockMvc.perform(put("/cvpt/equipment/componenttypes/" + id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(bodyJson));

		// validate put succeeded
		result.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// delete the newly created equipment component type
		// confirm delete succeeded 
		mockMvc.perform(delete(location))
			.andExpect(status().is(204));
	}

	@Test
	public void testEquipmentComponent() throws Exception
	{
		// get a known value for equipment component type id
		ResultActions result = mockMvc.perform(get("/cvpt/equipment/componenttypes"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first equipmentComponentTypeId value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String typeId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"equipmentComponentTypeId",
										"Test database missing a record for equipment component type. At least one equipment component type must be defined");

		// get a known value for equipment id
		result = mockMvc.perform(get("/cvpt/equipment"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first equipmentId value from JSON content
		responseContent = result.andReturn().getResponse().getContentAsString();
		String eqpId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"equipmentId",
										"Test database missing a record for equipment. At least one equipment must be defined");

		// create a new equipment component
		// use the known equipmentComponentTypeId and equipmentId just extracted
		String bodyJson = "{\"equipmentComponentTypeId\":" + typeId + ", \"equipmentId\":" + eqpId + ", \"description\":\"Test POST equipment component succeeded\"}";
		result = mockMvc.perform(post("/cvpt/equipment/" + eqpId + "/components")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// get id for new equipment component included in location url
		String location = result.andReturn().getResponse().getHeader("Location");
		String id = TestUtilityParse.getIdFromHeaderLocation(location, 
											"Incorrect location URL created from POST equipment component. Location: " + location);

		// test the group get
		mockMvc.perform(get("/cvpt/equipment/" + eqpId + "/components"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// test the single record get
		mockMvc.perform(get("/cvpt/equipment/components/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create update for equipment component
		bodyJson = "{\"equipmentComponentTypeId\":" + typeId + ", \"equipmentId\":" + eqpId + ", \"description\":\"Test PUT equipment component succeeded\"}";
		result = mockMvc.perform(put("/cvpt/equipment/components/" + id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(bodyJson));

		// validate put succeeded
		result.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// delete the newly created equipment component
		// confirm delete succeeded 
		mockMvc.perform(delete(location))
			.andExpect(status().is(204));
	}

}