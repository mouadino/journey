package com.journey.api.journey;

import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.Optional;

import com.journey.api.itinerary.Itinerary;
import com.journey.api.itinerary.ItineraryRepository;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JourneyServiceTest {

    private JourneyRepository jRepository;
    private ItineraryRepository itRepository;

    private JourneyService srv;

    @Before
    public void setUp() {
        jRepository = Mockito.mock(JourneyRepository.class);
        itRepository = Mockito.mock(ItineraryRepository.class);

        srv = new JourneyService(jRepository, itRepository);
    }

    @Test
    public void testSaveJourney() throws Exception {
        Journey savedJourney = new Journey("test");

        Mockito.when(jRepository.save(Mockito.any(Journey.class))).thenReturn(savedJourney);

        srv.save(savedJourney);
    }

    @Test
    public void testGetJourney() throws Exception {
        Journey savedJourney = new Journey("test");

        Mockito.when(jRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(savedJourney));

        Optional<Journey> resultJourney = srv.findById(1l);

        assertThat(resultJourney.isPresent(), Matchers.is(true));
        assertThat(resultJourney.get().getName(), Matchers.is("test"));
    }

    @Test
    public void testAddItineraryToExistingJourney() throws Exception {
        Journey savedJourney = new Journey("test");
        Itinerary itinerary = new Itinerary(new Date(), new Date());

        Mockito.when(jRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(savedJourney));
        Mockito.when(itRepository.save(Mockito.any(Itinerary.class))).thenReturn(itinerary);

        Itinerary resultItinerary = srv.addItinerary(1l, itinerary);

        assertThat(resultItinerary.getId(), Matchers.notNullValue());
    }

    @Test(expected = JourneyNotFoundException.class)
    public void testAddItineraryToUnknownJourney() throws Exception {
        Itinerary itinerary = new Itinerary(new Date(), new Date());

        Mockito.when(jRepository.findById(Mockito.anyLong())).thenThrow(new JourneyNotFoundException(1l));

        srv.addItinerary(1l, itinerary);
    }
    
}