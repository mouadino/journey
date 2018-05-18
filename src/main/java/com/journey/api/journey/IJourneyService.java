package com.journey.api.journey;

import com.journey.api.itinerary.Itinerary;

public interface IJourneyService {
    public Journey findById(long id) throws JourneyNotFoundException;

    public Journey create(final Journey j);

    public Itinerary addItinerary(long journeyId, Itinerary it) throws JourneyNotFoundException;
}