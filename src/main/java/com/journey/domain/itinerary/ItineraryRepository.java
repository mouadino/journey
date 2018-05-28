package com.journey.domain.itinerary;

import java.util.Optional;

import com.journey.domain.journey.Journey;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraryRepository extends CrudRepository<Itinerary, Long> {

    // TODO: Enable optimistic locking.
    @Modifying
    @Query("DELETE FROM Itinerary t WHERE t.id = :itineraryId AND t.journey.id = :#{#journey.id}")
    int delete(@Param("journey") Journey journey, @Param("itineraryId") long itineraryId);

    //@Query("SELECT t FROM Itinerary t WHERE t.id = :itineraryId AND t.journey.id = :journeyId")
    Optional<Itinerary> findByIdAndJourney(long itineraryID, Journey journey);
}