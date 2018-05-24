package com.journey.domain.journey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.journey.domain.itinerary.Itinerary;

@Entity
@Table(name = "journey")
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @NotNull
    private String name;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "journey",
        orphanRemoval = true)
    private List<Itinerary> itineraries = new ArrayList<>();

    public Journey() {}

    public Journey(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Itinerary> getItineraries() {
        return  Collections.unmodifiableList(itineraries);
    }

    public void setItineraries(List<Itinerary> itineraries) {
        this.itineraries = itineraries;
    }

    @Override
    public String toString() {
        return "Journey{" +
        "name='" + name + '\'' +
        '}';
    }
}