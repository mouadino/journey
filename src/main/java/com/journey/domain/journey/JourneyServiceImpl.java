package com.journey.domain.journey;

import java.util.Optional;

import com.journey.domain.itinerary.Itinerary;
import com.journey.domain.itinerary.ItineraryNotFoundException;
import com.journey.domain.itinerary.ItineraryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Journey journey = jRepository.findById(id).orElseThrow(() -> new JourneyNotFoundException(id));
        return journey;
    }

    public Journey create(final Journey j) {
        return jRepository.save(j);
    }

    @Transactional
    public Journey update(long journeyId, final Journey newJourney) {
        Journey journey = findById(journeyId);

        journey.setName(newJourney.getName());

        return jRepository.save(journey);
    }

    public void delete(long journeyId) {
        jRepository.deleteById(journeyId);
    }

    public Page<Journey> findPaginated(int page, int size) {
        return jRepository.findAll(PageRequest.of(page, size));
    }

    public Itinerary addItinerary(long journeyId, Itinerary it) throws JourneyNotFoundException {
        Journey journey = findById(journeyId);
        it.setJourney(journey);
        Itinerary savedItinerary = itRepository.save(it);
        return savedItinerary;
    }

    public Itinerary updateItinerary(long journeyId, long itineraryId, Itinerary newItinerary) throws JourneyNotFoundException, ItineraryNotFoundException {
        Journey journey = findById(journeyId);
        
        Itinerary itinerary = itRepository.findByIdAndJourney(itineraryId, journey)
                                          .orElseThrow(() -> new ItineraryNotFoundException(itineraryId));

        itinerary.setStart(newItinerary.getStart());
        itinerary.setEnd(newItinerary.getEnd());

        return itRepository.save(itinerary);
    }

    @Transactional
    public void removeItinerary(long journeyId, long itineraryId) throws JourneyNotFoundException, ItineraryNotFoundException {
        Journey journey = findById(journeyId);
        int deletedCount = itRepository.delete(journey, itineraryId);
        if (deletedCount != 1) {
            throw new ItineraryNotFoundException(itineraryId);
        }
    }
}