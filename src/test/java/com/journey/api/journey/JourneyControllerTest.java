package com.journey.api.journey;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.journey.domain.itinerary.Itinerary;
import com.journey.domain.journey.Journey;
import com.journey.domain.journey.JourneyService;
import com.journey.domain.journey.JourneyNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JourneyControllerTest {

	@MockBean
	JourneyService jService;

	private MockMvc mvc;

	@Autowired
    WebApplicationContext context;

	@Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

	@Test
	public void testGetJourney() throws Exception {
		Journey j = new Journey("test");
		given(jService.findById(anyLong())).willReturn(j);

		mvc.perform(get("/api/journey/1")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("test")));

		verify(jService).findById(1l);
	}

	@Test
	public void testGetUnknownJourney() throws Exception {
		given(jService.findById(anyLong())).willThrow(new JourneyNotFoundException(1l));

		mvc.perform(get("/api/journey/1")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

		verify(jService).findById(1l);
	}

	@Test
	public void testCreateNewJourney() throws Exception {
		Journey savedJourney = new Journey();
		given(jService.create(any(Journey.class))).willReturn(savedJourney);

		mvc.perform(post("/api/journey")
		.content("{\"name\": \"test\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(header().string("Location", is(String.format("/api/journey/%d", savedJourney.getId()))));

		verify(jService).create(any());
	}

	@Test
	public void testDeleteJourney() throws Exception {
        Mockito.doNothing().when(jService).delete(Mockito.anyLong());

		mvc.perform(delete("/api/journey/1"))
		.andExpect(status().isNoContent());

		verify(jService).delete(anyLong());
	}

	@Test
	public void testCreateJourneyWithBlankName_shouldFail() throws Exception {
		mvc.perform(post("/api/journey")
		.content("{\"name\": \"\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}

	@Test
	public void testAddItineraryToJourney() throws Exception {
		Itinerary savedItinerary = new Itinerary();
		given(jService.addItinerary(anyLong(), any(Itinerary.class))).willReturn(savedItinerary);

		mvc.perform(post("/api/journey/1/itinerary")
		.content("{\"start\": \"2018-05-19\", \"end\": \"2018-05-22\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(header().string("Location", is(String.format("/api/journey/1/itinerary/%d", savedItinerary.getId()))));
	}
}
