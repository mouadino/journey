package com.journey.domain.journey;

import com.journey.domain.itinerary.Itinerary;

import org.springframework.data.domain.Page;

public interface JourneyService {
    public Journey findById(long id) throws JourneyNotFoundException;

    public Journey create(final Journey j);

    public void delete(long journeyId);

    public Page<Journey> findPaginated(int page, int size);

    public Itinerary addItinerary(long journeyId, Itinerary it) throws JourneyNotFoundException;
}