package com.journey.domain.journey;

import java.util.Optional;

import com.journey.domain.itinerary.Itinerary;
import com.journey.domain.itinerary.ItineraryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class JourneyServiceImpl implements JourneyService {
    private final JourneyRepository jRepository;
    private final ItineraryRepository itRepository;

    @Autowired
    public JourneyServiceImpl(JourneyRepository repo, ItineraryRepository iRepository) {
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

    public void delete(long journeyId) {
        jRepository.deleteById(journeyId);
    }

    public Itinerary addItinerary(long journeyId, Itinerary it) throws JourneyNotFoundException {
        Journey journey = findById(journeyId);
        it.setJourney(journey);
        Itinerary savedItinerary = itRepository.save(it);
        return savedItinerary;
    }
}