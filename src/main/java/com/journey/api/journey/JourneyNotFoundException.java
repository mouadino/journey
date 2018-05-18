package com.journey.api.journey;

public class JourneyNotFoundException extends RuntimeException {
    JourneyNotFoundException(long journeyID) {
        super(String.format("journey %d not found", journeyID));
    }
}