package com.journey.infrastructure.journey;

import com.journey.domain.itinerary.Itinerary;
import com.journey.domain.journey.Journey;
import com.journey.domain.journey.JourneyRepositoryWithRefresh;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyRepository extends PagingAndSortingRepository<Journey, Long>, JourneyRepositoryWithRefresh {
    public void refresh(Itinerary it);
}