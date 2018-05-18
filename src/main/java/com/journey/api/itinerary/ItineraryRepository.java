package com.journey.api.itinerary;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraryRepository extends CrudRepository<Itinerary, Long> {}