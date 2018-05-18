package com.journey.api.journey;

import java.util.Optional;

import com.journey.api.itinerary.Itinerary;
import com.journey.api.itinerary.ItineraryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JourneyService implements IJourneyService {
    private final Logger logger = LoggerFactory.getLogger(JourneyService.class);
    private final JourneyRepository jRepository;
    private final ItineraryRepository itRepository;

    @Autowired
    public JourneyService(JourneyRepository repo, ItineraryRepository iRepository) {
        this.jRepository = repo;
        this.itRepository = iRepository;
    }

    public Journey findById(long id) {
        Optional<Journey> journey = jRepository.findById(id);
        if (! journey.isPresent()) {
            throw new JourneyNotFoundException(id);
        }
        return journey.get();
    }

    public Journey create(final Journey j) {
        return jRepository.save(j);
    }

    public Itinerary addItinerary(long journeyId, Itinerary it) throws JourneyNotFoundException {
        Journey journey = findById(journeyId);
        it.setJourney(journey);
        Itinerary savedItinerary = itRepository.save(it);
        return savedItinerary;
    }
}