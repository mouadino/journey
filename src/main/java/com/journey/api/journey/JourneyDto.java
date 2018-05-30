package com.journey.api.journey;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

public class JourneyDto {
    @Getter @Setter
    private long id;

    @Getter @Setter
    @NotBlank @NotNull
    private String name;

    @Setter
    private List<ItineraryDto> itineraries;

    public List<ItineraryDto> getItineraries() {
        if (this.itineraries != null) {
            this.itineraries.sort(((it1, it2) -> it1.getStart().compareTo(it2.getStart())));
        }
        return this.itineraries;
    }
}