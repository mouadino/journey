package com.journey.domain.itinerary;

public class ItineraryNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 2L;
    
    public ItineraryNotFoundException(long itineraryID) {
        super(String.format("Itinerary %d not found", itineraryID));
    }
}