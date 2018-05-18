package com.journey.api.journey;

import java.util.Optional;

import com.journey.api.itinerary.Itinerary;

public interface IJourneyService {
    public Optional<Journey> findById(long id);

    public Journey save(final Journey j);

    public Itinerary addItinerary(long journeyId, Itinerary it) throws JourneyNotFoundException;
}