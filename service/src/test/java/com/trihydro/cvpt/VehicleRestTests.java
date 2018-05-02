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
public class VehicleRestTests
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
	public void testVehicleClass() throws Exception
	{
		// test the group get 
		ResultActions result = mockMvc.perform(get("/cvpt/vehicleclasses"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first vehicleClassId value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String id = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"vehicleClassId",
										"Test database missing a record for vehicle_class. At least one vehicle class must be defined");

		// test the single record get
		result = mockMvc.perform(get("/cvpt/vehicleclasses/" + id))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create a new vehicle class
		String bodyJson = "{\"vehicleClassNumber\":100, \"vehicleClass\":\"Test POST vehicleClass succeeded\"}";
		result = mockMvc.perform(post("/cvpt/vehicleclasses")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// extract the newly generated location url for this new equipment type
		String location = result.andReturn().getResponse().getHeader("Location");

		// get id for new equipment type included in location url
		id = TestUtilityParse.getIdFromHeaderLocation(location, 
									"Incorrect location URL created from POST vehicle_class. Location: " + location);

		// create update for vehicle class
		bodyJson = "{\"vehicleClassNumber\":100, \"vehicleClass\":\"Test PUT vehicleClass succeeded\"}";
		result = mockMvc.perform(put("/cvpt/vehicleclasses/" + id)
									.contentType(MediaType.APPLICATION_JSON)
									.content(bodyJson));

		// validate put succeeded
		result.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// delete the newly created equipment type
		// confirm delete succeeded 
		mockMvc.perform(delete(location))
			.andExpect(status().is(204));
	}

	
	@Test
	public void testVehicle() throws Exception
	{
		// get a known value for vehicle class id
		ResultActions result = mockMvc.perform(get("/cvpt/vehicleclasses"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first vehicleClassId value from JSON content
		String responseContent = result.andReturn().getResponse().getContentAsString();
		String classId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"vehicleClassId",
										"Test database missing a record for vehicle_class. At least one vehicle_class must be defined");

		// get a known value for organiztion id
		result = mockMvc.perform(get("/cvpt/organizations"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first orgainzationId value from JSON content
		responseContent = result.andReturn().getResponse().getContentAsString();
		String orgId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"organizationId",
										"Test database missing a record for organization. At least one organization must be defined");

		// create a new vehicle
		// use the known vehicleClassId and organizationId just extracted
		String bodyJson = "{\"id\":\"Test POST Vehicle Succeded\",\"vehicleClassId\":" + classId + ", \"organizationId\":" + orgId + "}";
		result = mockMvc.perform(post("/cvpt/vehicles")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// extract the newly generated location url for this new vehicle
		String location = result.andReturn().getResponse().getHeader("Location");

		// get id for new vehicle included in location url
		String id = TestUtilityParse.getIdFromHeaderLocation(location, 
											"Incorrect location URL created from POST vehicle. Location: " + location);

		// test the group get
		mockMvc.perform(get("/cvpt/vehicles"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// test the single record get
		mockMvc.perform(get("/cvpt/vehicles/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create update for vehicle
		bodyJson = "{\"id\":\"Test PUT Vehicle Succeded\",\"vehicleClassId\":" + classId + ", \"organizationId\":" + orgId + "}";
		result = mockMvc.perform(put("/cvpt/vehicles/" + id)
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
	public void testVehicleEquipment() throws Exception
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

		// get a known value for equipment id
		result = mockMvc.perform(get("/cvpt/equipment"))
								.andExpect(status().isOk())
								.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// extract first equipmentId value from JSON content
		responseContent = result.andReturn().getResponse().getContentAsString();
		String eqpId = TestUtilityParse.getIdFromBodyContent(responseContent, 
										"equipmentId",
										"Test database missing a record for organization. At least one organization must be defined");

		// create a new vehicle equipment
		// use the known vehicleId and equipmentId just extracted
		String bodyJson = "{\"vehicleId\":" + vehId + ", \"equipmentId\":" + eqpId + "}";
		result = mockMvc.perform(post("/cvpt/vehicles/" + vehId + "/equipment")
												.contentType(MediaType.APPLICATION_JSON)
												.content(bodyJson));
		// validate post succeeded 
		result.andExpect(status().is(201));

		// extract the newly generated location url for this new vehicle
		String location = result.andReturn().getResponse().getHeader("Location");

		// get id for new vehicle equipment included in location url
		String id = TestUtilityParse.getIdFromHeaderLocation(location, 
											"Incorrect location URL created from POST vehicle equipment. Location: " + location);

		// test the group get
		mockMvc.perform(get("/cvpt/vehicles/" + vehId + "/equipment"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// test the single record get
		mockMvc.perform(get("/cvpt/vehicles/equipment/" + id))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		// create update for vehicle equipment
		bodyJson = "{\"vehicleId\":" + vehId + ", \"equipmentId\":" + eqpId + "}";
		result = mockMvc.perform(put("/cvpt/vehicles/equipment/" + id)
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