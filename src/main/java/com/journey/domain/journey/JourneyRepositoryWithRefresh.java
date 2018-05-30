package com.journey.domain.journey;

import com.journey.domain.itinerary.Itinerary;

public interface JourneyRepositoryWithRefresh {
    public void refresh(Itinerary it);
}