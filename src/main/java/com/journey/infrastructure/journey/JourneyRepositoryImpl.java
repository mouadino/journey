package com.journey.infrastructure.journey;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.journey.domain.itinerary.Itinerary;
import com.journey.domain.journey.Journey;

import org.springframework.stereotype.Repository;

@Repository
public class JourneyRepositoryImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public void refresh(Itinerary it) {
        this.entityManager.flush();
        this.entityManager.refresh(it.getJourney());
    }
}