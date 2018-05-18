package com.journey.api.journey;

import java.net.URI;

import javax.validation.Valid;

import com.journey.api.itinerary.Itinerary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JourneyController {
    private static final Logger logger = LoggerFactory.getLogger(JourneyController.class);

    private final IJourneyService svc;

    @Autowired
    public JourneyController(IJourneyService repo) {
        this.svc = repo;
    }

    @RequestMapping(value = "/journey/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable long id) {
        Journey journey = svc.findById(id);
        return ResponseEntity.ok(journey);
    }

    @RequestMapping(value = "/journey", method = RequestMethod.POST)
    public ResponseEntity<?> post(@Valid @RequestBody Journey journey) {
        Journey savedJourney = svc.create(journey);
        URI location = URI.create(String.format("/api/journey/%d", savedJourney.getId()));
        return ResponseEntity.created(location).build();        
    }

    @RequestMapping(value = "/journey/{journeyId}/itinerary", method = RequestMethod.POST)
    public ResponseEntity<?> addItinerary(@PathVariable long journeyId, @Valid @RequestBody Itinerary it) {
        Itinerary savedItinerary = svc.addItinerary(journeyId, it);
        URI location = URI.create(String.format("/api/journey/%d/itinerary/%d", journeyId, savedItinerary.getId()));
        return ResponseEntity.created(location).build();        
    }
}