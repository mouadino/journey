package com.journey.domain.journey;

import com.journey.domain.itinerary.Itinerary;
import com.journey.domain.itinerary.ItineraryNotFoundException;
import com.journey.infrastructure.journey.JourneyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class JourneyServiceImpl implements JourneyService {
    private final JourneyRepository jRepository;

    @Autowired
    public JourneyServiceImpl(JourneyRepository repo) {
        this.jRepository = repo;
    }

    @Transactional(readOnly=true)
    public Journey findById(long id) {
        return jRepository.findById(id).orElseThrow(() -> new JourneyNotFoundException(id));
    }

    @Transactional
    public Journey create(final Journey j) {
        return jRepository.save(j);
    }

    @Transactional
    public Journey update(long journeyId, final Journey newJourney) {
        // TODO: Pass version too for optimistic locking.
        Journey journey = findById(journeyId);

        newJourney.setId(journey.getId());

        return jRepository.save(newJourney);
    }

    @Transactional
    public void delete(long journeyId) {
        // TODO: Pass version too for optimistic locking.
        jRepository.deleteById(journeyId);
    }

    @Transactional(readOnly=true)
    public Page<Journey> findPaginated(int page, int size) {
        return jRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public Itinerary addItinerary(long journeyId, Itinerary it) throws JourneyNotFoundException {
        // TODO: Pass version too for optimistic locking.
        Journey journey = findById(journeyId);

        journey.addItinerary(it);
        jRepository.save(journey);

        jRepository.refresh(it);

        // XXX: Ugly hack to get the id of the newly inserted itinerary.
        // XXX: Make test flay as ordered is not shuffled after all it's a Set.
        return (Itinerary) journey.getItineraries().toArray()[journey.getItineraries().size() - 1];
    }

    @Transactional
    public Itinerary updateItinerary(long journeyId, long itineraryId, Itinerary newItinerary) throws JourneyNotFoundException, ItineraryNotFoundException {
        // TODO: Pass version too for optimistic locking.
        // FIXME: Remove usage of itRepository.
        Journey journey = findById(journeyId);
        
        Itinerary replacedItinerary = journey.replaceItinerary(itineraryId, newItinerary);
        jRepository.save(journey);

        // TODO: Do I need this?
        jRepository.refresh(replacedItinerary);

        return replacedItinerary;
    }

    @Transactional
    public void removeItinerary(long journeyId, long itineraryId) throws JourneyNotFoundException, ItineraryNotFoundException {
        // TODO: Add Optimistic locking.
        Journey journey = findById(journeyId);

        journey.removeItinerary(itineraryId);

        jRepository.save(journey);
    }
}