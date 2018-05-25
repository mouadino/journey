package com.journey;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.journey.Application;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@Sql(scripts = "classpath:truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
public class JourneyAPITest {

	// FIXME: Same ID used in test/resources/fixtures/*.sql.
	// private final static long FIXTURE_JOURNEY_ID = 1234;

	private MockMvc mvc;

	@Autowired
    WebApplicationContext context;

	@Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

	@Test
	public void testANewlyCreatedJourneyShouldBeReturned() throws Exception {
		MvcResult res = mvc.perform(post("/api/journey")
		.content("{\"name\": \"My First Journey\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
        .andExpect(header().string("Location", Matchers.startsWith("/api/journey/")))
        .andReturn();

        String journeyURI = res.getResponse().getHeader("Location");

        mvc.perform(get(journeyURI)
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", Matchers.is("My First Journey")));

		mvc.perform(delete(journeyURI)).andExpect(status().isNoContent());
	}

	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:fixtures/journies.sql")
	public void testAddingItineraryShouldBeReturnedInJourney() throws Exception {
		//String journeyURI = String.format("api/journey/%d", FIXTURE_JOURNEY_ID);

		// FIXME: Use fixtures!
		MvcResult res = mvc.perform(post("/api/journey")
		.content("{\"name\": \"My First Journey\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
        .andExpect(header().string("Location", Matchers.startsWith("/api/journey/")))
        .andReturn();

		String journeyURI = res.getResponse().getHeader("Location");
		String itineraryURI = String.format("%s/%s", journeyURI, "itinerary");

		mvc.perform(post(itineraryURI)
		.content("{\"start\": \"2018-06-22T20:00Z\", \"end\": \"2018-06-23T10:00Z\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(header().string("Location", Matchers.startsWith(itineraryURI)));
		
		mvc.perform(post(itineraryURI)
		.content("{\"start\": \"2018-05-17T20:00Z\", \"end\": \"2018-05-19T10:00Z\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(header().string("Location", Matchers.startsWith(itineraryURI)));

        mvc.perform(get(journeyURI)
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.itineraries", Matchers.hasSize(2)))
		.andExpect(jsonPath("$.itineraries[0].start", Matchers.is("2018-05-17T20:00Z")))
		.andExpect(jsonPath("$.itineraries[0].end", Matchers.is("2018-05-19T10:00Z")))
		.andExpect(jsonPath("$.itineraries[1].start", Matchers.is("2018-06-22T20:00Z")))
		.andExpect(jsonPath("$.itineraries[1].end", Matchers.is("2018-06-23T10:00Z")));
	}
}