package com.journey.domain.journey;

public class JourneyNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public JourneyNotFoundException(long journeyID) {
        super(String.format("journey %d not found", journeyID));
    }
}