package com.journey.domain.itinerary;

public class ItineraryAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = 3L;
    
    public ItineraryAlreadyExistsException(long itineraryID) {
        super(String.format("Itinerary %d already exists", itineraryID));
    }
}