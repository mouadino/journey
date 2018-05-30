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

import com.journey.domain.itinerary.Itinerary;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @EqualsAndHashCode(of = {"id", "name"}) @ToString
@Entity
@Table(name = "journey")
public class Journey {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter @Setter
    private String name;

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "journey",
        orphanRemoval = true)
    private List<Itinerary> itineraries = new ArrayList<>();

    public Journey(String name) {
        this.name = name;
    }

    public List<Itinerary> getItineraries() {
        return  Collections.unmodifiableList(itineraries);
    }
}