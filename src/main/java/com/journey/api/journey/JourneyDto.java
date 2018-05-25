package com.journey.api.journey;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.ReadOnlyProperty;

public class JourneyDto {
    @ReadOnlyProperty
    private long id;

    @ReadOnlyProperty
    @NotBlank
    @NotNull
    private String name;
    private List<ItineraryDto> itineraries;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItineraryDto> getItineraries() {
        if (this.itineraries != null) {
            this.itineraries.sort(((it1, it2) -> it1.getStart().compareTo(it2.getStart())));
        }
        return this.itineraries;
    }

    public void setItineraries(List<ItineraryDto> itineraries) {
        this.itineraries = itineraries;
    }
}