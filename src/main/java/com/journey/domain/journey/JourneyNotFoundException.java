package com.journey.domain.journey;

public class JourneyNotFoundException extends RuntimeException {
    public JourneyNotFoundException(long journeyID) {
        super(String.format("journey %d not found", journeyID));
    }
}