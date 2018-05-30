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


/* This test class includes tests that are better expressed with using a real repository instead
of a mock.
*/
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
            Itinerary.builder()
                     .start(new Date())
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
            Itinerary.builder()
                     .start(new Date())
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

    @Test
    @SuppressWarnings("deprecation")
    public void testUpdateItinerary() {
        Journey journey = new Journey("test");
        entityManager.persist(journey);
        entityManager.refresh(journey);

        long itineraryId = (long)entityManager.persistAndGetId(
            Itinerary.builder()
                     .start(new Date())
                     .journey(journey)
                     .build()
        );

        Itinerary newItinerary = Itinerary.builder()
                                          .start(new Date(2018, 05, 22))
                                          .end(new Date(2018, 05, 30))
                                          .journey(journey)
                                          .build();

        Itinerary persisedItinerary = srv.updateItinerary(journey.getId(), itineraryId, newItinerary);

        assertThat(persisedItinerary, Matchers.is(newItinerary));
    }

    @Test(expected = ItineraryNotFoundException.class)
    public void testUpdateItineraryWithUnknownItinerary() {
        Journey journey = new Journey("test");
        entityManager.persist(journey);
        entityManager.refresh(journey);

        srv.updateItinerary(journey.getId(), 10234, Itinerary.builder().build());
    }

    @Test(expected = JourneyNotFoundException.class)
    public void testUpdateItineraryWithUnknownJourney() {
        Journey journey = new Journey("test");
        entityManager.persist(journey);
        entityManager.refresh(journey);

        long itineraryId = (long)entityManager.persistAndGetId(
            Itinerary.builder()
                     .start(new Date())
                     .journey(journey)
                     .build()
        );

        srv.updateItinerary(10234, itineraryId, Itinerary.builder().build());
    }
}
