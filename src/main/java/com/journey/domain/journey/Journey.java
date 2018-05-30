package com.journey.domain.journey;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.journey.domain.itinerary.Itinerary;
import com.journey.domain.itinerary.ItineraryAlreadyExistsException;
import com.journey.domain.itinerary.ItineraryNotFoundException;

import org.springframework.util.Assert;

// TODO: Add validator for a maximum amount of itineraries per journey.
@Entity
@Table(name = "journey")
public class Journey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Version
    private long version = 1;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.ALL,
        orphanRemoval = true)
    @JoinColumn(name = "journey_id", nullable = false)
    private Set<Itinerary> itineraries = new LinkedHashSet<>();

    public Journey() {
        super();
    }

    public Journey(String name) {
        super();
        
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Itinerary> getItineraries() {
        return Collections.unmodifiableSet(itineraries);
    }

    public void addItinerary(Itinerary newItinerary) {
        // FIXME: This requires loading all itinerary!
        Assert.isNull(newItinerary.getJourney(), "itinerary cannot be added with journey already set.");
        
        newItinerary.setJourney(this);

        boolean added = itineraries.add(newItinerary);
        if (! added) {
            throw new ItineraryAlreadyExistsException(newItinerary.getId());
        }
    }

    public void removeItinerary(long itineraryId) throws ItineraryNotFoundException {
        Itinerary itinerary = findItinerary(itineraryId);
        boolean removed = itineraries.remove(itinerary);
        if (! removed) {
            throw new ItineraryNotFoundException(itineraryId);
        }
    }
    
    public Itinerary findItinerary(long itineraryId) throws ItineraryNotFoundException {
        // FIXME: This requires loading all itinerary!
        Itinerary found = null;
        for (Itinerary it: itineraries) {
            if (it.getId() == itineraryId) {
                found = it;
                break;
            }
        } 

        if (found == null) {
            throw new ItineraryNotFoundException(itineraryId);
        }
        return found;
    }

    public Itinerary replaceItinerary(long itineraryId, final Itinerary newItinerary) throws ItineraryNotFoundException {
        Assert.isNull(newItinerary.getJourney(), "Itinerary with an already set journey is not acceptable.");

        // FIXME: This doesn't trigger an update of journey version.
        /*Itinerary persistedItinerary = findItinerary(itineraryId);
        persistedItinerary.setEnd(newItinerary.getEnd());
        persistedItinerary.setStart(newItinerary.getStart());
        */

        // FIXME: This will create a new entity with a different id! 
        removeItinerary(itineraryId);
        addItinerary(newItinerary);

        return newItinerary;
    }

    public long getVersion() { return version; }

    // TODO: Do I need this?
    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Journey{" +
        " id=" + id +
        " , name='" + name + '\'' +
        " , version=" + version +
        " , itineraries=" + itineraries +
        '}';
    }

    @Override
    public boolean equals(Object that) {
        if (that == this) return true;
        if (! (that instanceof Journey)) return false;

        Journey thatJourney = (Journey) that;

        return this.id == thatJourney.id
            && this.name.equals(thatJourney.name) 
            && this.version == thatJourney.version;
    }
}