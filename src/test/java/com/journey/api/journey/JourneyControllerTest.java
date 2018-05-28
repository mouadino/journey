package com.journey.api.journey;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

import com.journey.domain.itinerary.Itinerary;
import com.journey.domain.itinerary.ItineraryNotFoundException;
import com.journey.domain.journey.Journey;
import com.journey.domain.journey.JourneyNotFoundException;
import com.journey.domain.journey.JourneyService;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

		mvc.perform(get("/api/journies/1")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("test")));

		verify(jService).findById(1l);
	}

	@Test
	public void testGetUnknownJourney() throws Exception {
		given(jService.findById(anyLong())).willThrow(new JourneyNotFoundException(1l));

		mvc.perform(get("/api/journies/1")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

		verify(jService).findById(1l);
	}

	@Test
	public void testCreateNewJourney() throws Exception {
		Journey savedJourney = new Journey();
		given(jService.create(any(Journey.class))).willReturn(savedJourney);

		mvc.perform(post("/api/journies")
		.content("{\"name\": \"test\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(header().string("Location", is(String.format("/api/journies/%d", savedJourney.getId()))));

		verify(jService).create(any());
	}

	@Test
	public void testListJournies() throws Exception {
		List<Journey> journies = new ArrayList<Journey>(Arrays.asList(
			new Journey(),
			new Journey()
		));
		Page<Journey> pagedJournies = new PageImpl<Journey>(journies);
		given(jService.findPaginated(anyInt(), anyInt())).willReturn(pagedJournies);

		mvc.perform(get("/api/journies")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(journies.size())));

		verify(jService).findPaginated(0, 10);
	}

	@Test
	public void testListJourniesWithPagination() throws Exception {
		List<Journey> journies = new ArrayList<Journey>(Arrays.asList(
			new Journey(),
			new Journey()
		));
		Page<Journey> pagedJournies = new PageImpl<Journey>(journies);
		given(jService.findPaginated(anyInt(), anyInt())).willReturn(pagedJournies);

		mvc.perform(get("/api/journies?page=1&size=3")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(journies.size())));

		verify(jService).findPaginated(1, 3);
	}

	@Test
	public void testDeleteJourney() throws Exception {
        Mockito.doNothing().when(jService).delete(Mockito.anyLong());

		mvc.perform(delete("/api/journies/1"))
		.andExpect(status().isNoContent());

		verify(jService).delete(1);
	}

	@Test
	public void testCreateJourneyWithBlankName_shouldFail() throws Exception {
		mvc.perform(post("/api/journies")
		.content("{\"name\": \"\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest());
	}

	@Test
	public void testAddItineraryToJourney() throws Exception {
		Itinerary savedItinerary = new Itinerary();
		given(jService.addItinerary(anyLong(), any(Itinerary.class))).willReturn(savedItinerary);

		mvc.perform(post("/api/journies/1/itinerary")
		.content("{\"start\": \"2018-05-19T06:30Z\", \"end\": \"2018-05-22T10:00Z\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(header().string("Location", is(String.format("/api/journies/1/itinerary/%d", savedItinerary.getId()))));
	}

	@Test
	public void testDeleteItineraryFromJourney() throws Exception {
		Mockito.doNothing().when(jService).removeItinerary(anyLong(), anyLong());

		mvc.perform(delete("/api/journies/55/itinerary/2")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());

		verify(jService).removeItinerary(55, 2);
	}

	@Test
	public void testDeleteItineraryFromUnknownJourney() throws Exception {
		Mockito.doThrow(new JourneyNotFoundException(55)).when(jService).removeItinerary(anyLong(), anyLong());

		mvc.perform(delete("/api/journies/55/itinerary/2")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

		verify(jService).removeItinerary(55, 2);
	}

	@Test
	public void testDeleteItineraryFromUnknownItinerary() throws Exception {
		Mockito.doThrow(new ItineraryNotFoundException(2)).when(jService).removeItinerary(anyLong(), anyLong());

		mvc.perform(delete("/api/journies/55/itinerary/2")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());

		verify(jService).removeItinerary(55, 2);
	}

	@Test
	public void testUpdateJourney() throws Exception {
		Journey savedJourney = new Journey("test 2");
		given(jService.update(anyLong(), any(Journey.class))).willReturn(savedJourney);

		mvc.perform(put("/api/journies/1")
		.content("{\"name\": \"test 2\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("test 2")));		
	}

	@Test
	public void testUpdateUnknownJourney() throws Exception {
		given(jService.update(anyLong(), any(Journey.class))).willThrow(new JourneyNotFoundException(1));

		mvc.perform(put("/api/journies/1")
		.content("{\"name\": \"test 2\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateItinerary() throws Exception {
		Itinerary savedItinerary = Itinerary.builder(new Date()).build();
		given(jService.updateItinerary(anyLong(), anyLong(), any(Itinerary.class))).willReturn(savedItinerary);

		mvc.perform(put("/api/journies/1/itinerary/2")
		.content("{\"start\": \"2018-05-19T06:30Z\", \"end\": \"2018-05-22T10:00Z\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	@Test
	public void testUpdateItineraryOfUnknownJourney() throws Exception {
		given(jService.updateItinerary(anyLong(), anyLong(), any(Itinerary.class))).willThrow(new JourneyNotFoundException(1));

		mvc.perform(put("/api/journies/1/itinerary/2")
		.content("{\"start\": \"2018-05-19T06:30Z\", \"end\": \"2018-05-22T10:00Z\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}

	@Test
	public void testUpdateUknownItinerary() throws Exception {
		given(jService.updateItinerary(anyLong(), anyLong(), any(Itinerary.class))).willThrow(new ItineraryNotFoundException(1));

		mvc.perform(put("/api/journies/1/itinerary/2")
		.content("{\"start\": \"2018-05-19T06:30Z\", \"end\": \"2018-05-22T10:00Z\"}")
		.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}
}
