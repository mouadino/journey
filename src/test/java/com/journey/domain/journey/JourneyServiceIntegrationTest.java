package com.journey.domain.journey;

import static org.junit.Assert.assertThat;

import java.util.Date;

import com.journey.domain.itinerary.Itinerary;
import com.journey.domain.itinerary.ItineraryNotFoundException;
import com.journey.domain.itinerary.ItineraryRepository;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class JourneyServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItineraryRepository itRepository;

    @Autowired
    private JourneyRepository jRepository;

    private JourneyService srv;

    @Before
    public void setUp() {
        srv = new JourneyServiceImpl(jRepository, itRepository);
    }

    void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testDeleteItinerarySuccesfully() {
        Journey journey = new Journey("test");
        entityManager.persist(journey);
        entityManager.refresh(journey);

        long itineraryId = (long)entityManager.persistAndGetId(
            Itinerary.builder(new Date())
                     .journey(journey)
                     .build()
        );

        srv.removeItinerary(journey.getId(), itineraryId);

        flushAndClear();

        Itinerary savedItinerary = entityManager.find(Itinerary.class, itineraryId);
        assertThat(savedItinerary, Matchers.nullValue());
    }

    @Test(expected = JourneyNotFoundException.class)
    public void testDeleteItineraryOfUnknownJourney() {
        Journey journey = new Journey("test");
        entityManager.persist(journey);
        entityManager.refresh(journey);

        long itineraryId = (long)entityManager.persistAndGetId(
            Itinerary.builder(new Date())
                     .journey(journey)
                     .build()
        );

        srv.removeItinerary(10234, itineraryId);
    }

    @Test(expected = ItineraryNotFoundException.class)
    public void testDeleteUnknownItinerary() {
        Journey journey = new Journey("test");
        entityManager.persist(journey);
        entityManager.refresh(journey);

        srv.removeItinerary(journey.getId(), 10234);
    }
}
