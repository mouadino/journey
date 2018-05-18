package com.journey;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.journey.api.Application;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class JourneyAPITest {

	private MockMvc mvc;

	@Autowired
    WebApplicationContext context;

	@Before
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

	@Test
	public void testCreateAndGetJourney() throws Exception {
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
	}
    
}