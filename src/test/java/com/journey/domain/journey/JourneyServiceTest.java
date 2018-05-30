package com.journey.domain.journey;

import static org.junit.Assert.assertThat;

import java.util.Optional;

import com.journey.domain.itinerary.Itinerary;
import com.journey.infrastructure.journey.JourneyRepository;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JourneyServiceTest {

    private JourneyRepository jRepository;

    private JourneyService srv;

    @Before
    public void setUp() {
        jRepository = Mockito.mock(JourneyRepository.class);

        srv = new JourneyServiceImpl(jRepository);
    }

    @Test
    public void testSaveJourney() throws Exception {
        Journey savedJourney = new Journey("test");

        Mockito.when(jRepository.save(Mockito.any(Journey.class))).thenReturn(savedJourney);

        srv.create(savedJourney);
    }

    @Test
    public void testGetJourney() throws Exception {
        Journey savedJourney = new Journey("test");

        Mockito.when(jRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(savedJourney));

        Journey resultJourney = srv.findById(1l);

        assertThat(resultJourney.getName(), Matchers.is("test"));
    }

    @Test
    public void testAddItineraryToExistingJourney() throws Exception {
        Journey savedJourney = new Journey("test");
        Itinerary itinerary = new Itinerary();

        Mockito.when(jRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(savedJourney));
        Mockito.when(jRepository.save(Mockito.any(Journey.class))).thenReturn(savedJourney);

        Itinerary resultItinerary = srv.addItinerary(1l, itinerary);

        assertThat(resultItinerary.getId(), Matchers.notNullValue());
    }

    @Test(expected = JourneyNotFoundException.class)
    public void testAddItineraryToUnknownJourney() throws Exception {
        Itinerary itinerary = new Itinerary();

        Mockito.when(jRepository.findById(Mockito.anyLong())).thenThrow(new JourneyNotFoundException(1l));

        srv.addItinerary(1l, itinerary);
    }

    @Test
    public void testDeleteJourney() throws Exception {
        Mockito.doNothing().when(jRepository).deleteById(Mockito.anyLong());
        
        srv.delete(1);
    }

    @Test(expected = JourneyNotFoundException.class)
    public void testUpdateUnknownJourney() throws Exception {
        Mockito.when(jRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        srv.update(1l, new Journey());
    }

    @Test
    public void testUpdateJourney() throws Exception {
        Journey savedJourney = new Journey("test");
        Journey newJourney = new Journey("test 2");

        Mockito.when(jRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(savedJourney));
        Mockito.when(jRepository.save(Mockito.any())).thenReturn(newJourney);

        Journey returnedJourney = srv.update(1l, newJourney);

        assertThat(returnedJourney, Matchers.is(newJourney));
    }
}