package com.journey.domain.journey;

import com.journey.domain.itinerary.Itinerary;

public interface JourneyService {
    public Journey findById(long id) throws JourneyNotFoundException;

    public Journey create(final Journey j);

    public void delete(long journeyId);

    public Itinerary addItinerary(long journeyId, Itinerary it) throws JourneyNotFoundException;
}