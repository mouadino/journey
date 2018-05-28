package com.journey.api.journey;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.journey.domain.itinerary.Itinerary;
import com.journey.domain.itinerary.ItineraryNotFoundException;
import com.journey.domain.journey.Journey;
import com.journey.domain.journey.JourneyNotFoundException;
import com.journey.domain.journey.JourneyService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JourneyController {

    private final JourneyService svc;
    private final ModelMapper modelMapper;

    @Autowired
    public JourneyController(JourneyService repo, ModelMapper modelMapper) {
        this.svc = repo;
        this.modelMapper = modelMapper;
    }

    private JourneyDto convertToDto(Journey journey) {
        JourneyDto journeyDto = modelMapper.map(journey, JourneyDto.class);

        List<ItineraryDto> itineraries = new ArrayList<>();
        for (Itinerary it: journey.getItineraries()) {
            itineraries.add(modelMapper.map(it, ItineraryDto.class));
        }
        journeyDto.setItineraries(itineraries);
        return journeyDto;
    }

    private ItineraryDto convertToDto(Itinerary itinerary) {
        return modelMapper.map(itinerary, ItineraryDto.class);
    }

    private Journey convertToEntity(JourneyDto journeyDto)  {
        Journey journey = modelMapper.map(journeyDto, Journey.class);
        return journey;
    }

    private Itinerary convertToEntity(ItineraryDto itineraryDto) {
        Itinerary it = modelMapper.map(itineraryDto, Itinerary.class);
        return it;
    }

    @RequestMapping(value = "/journies/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getJourney(@PathVariable long id) {
        Journey journey = svc.findById(id);
        return ResponseEntity.ok(convertToDto(journey));
    }

    @RequestMapping(value = "/journies", method = RequestMethod.POST)
    public ResponseEntity<?> createJourney(@Valid @RequestBody JourneyDto journeyDto) {
        Journey journey = convertToEntity(journeyDto);
        Journey savedJourney = svc.create(journey);
        URI location = URI.create(String.format("/api/journies/%d", savedJourney.getId()));
        return ResponseEntity.created(location).build();        
    }

    @RequestMapping(value = "/journies/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateJourney(@PathVariable long id, @Valid @RequestBody JourneyDto journeyDto) {
        Journey newJourney = convertToEntity(journeyDto);
        Journey savedJourney = svc.update(id, newJourney);
        return ResponseEntity.ok(convertToDto(savedJourney));        
    }

    @RequestMapping(value = "journies/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeJourney(@PathVariable long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "journies", method = RequestMethod.GET)
    public ResponseEntity<?> listJourney(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        Page<Journey> resultPage = svc.findPaginated(page.orElse(0), size.orElse(10));
        return ResponseEntity.ok(resultPage.getContent());
    } 

    @RequestMapping(value = "/journies/{journeyId}/itinerary", method = RequestMethod.POST)
    public ResponseEntity<?> addItinerary(@PathVariable long journeyId, @Valid @RequestBody ItineraryDto itineraryDto) {
        Itinerary it = convertToEntity(itineraryDto);
        Itinerary savedItinerary = svc.addItinerary(journeyId, it);
        URI location = URI.create(String.format("/api/journies/%d/itinerary/%d", journeyId, savedItinerary.getId()));
        return ResponseEntity.created(location).build();        
    }

    @RequestMapping(value = "/journies/{journeyId}/itinerary/{itineraryId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateItinerary(@PathVariable long journeyId, @PathVariable long itineraryId, @Valid @RequestBody ItineraryDto itineraryDto) {
        Itinerary newIt = convertToEntity(itineraryDto);

        Itinerary savedItinerary = svc.updateItinerary(journeyId, itineraryId, newIt);
        return  ResponseEntity.ok(convertToDto(savedItinerary));          
    }

    @RequestMapping(value = "/journies/{journeyId}/itinerary/{itineraryId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeItinerary(@PathVariable long journeyId, @PathVariable long itineraryId) {
        svc.removeItinerary(journeyId, itineraryId);
        return ResponseEntity.noContent().build();        
    }
}