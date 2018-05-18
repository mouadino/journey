package com.journey.api.journey;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such journey")
public class JourneyNotFoundException extends RuntimeException {
    JourneyNotFoundException(long journeyID) {
        super(String.format("journey %d not found", journeyID));
    }
}