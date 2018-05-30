package com.journey.domain.itinerary;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.journey.domain.journey.Journey;

// TODO: Add unique constraint for start/end/journey.
// TODO: Should I model itinerary as value object?
@ValidItinerary
@Entity
@Table(name = "itinerary")
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; 

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")  // end is a keyword in PSQL.
    private Date end;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "journey_id", insertable = false, updatable = false)
    private Journey journey;
    
    public Itinerary() {
        super();
    }

    public long getId() {
        return id;
    }
    
    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey j) {
        this.journey = j;
    }

    @Override
    public boolean equals(Object that) {
        if (that == this) return true;
        if (! (that instanceof Itinerary)) return false;

        Itinerary thatItinerary = (Itinerary) that;

        return 
            this.id == thatItinerary.id
            && this.start.equals(thatItinerary.start)
            && this.end.equals(thatItinerary.end)
            && this.journey.equals(thatItinerary.journey);
    }

    @Override
    public int hashCode() {
         return Objects.hash(id, start, end, journey);
    }

    @Override
    public String toString() {
        return "Itinerary{" +
        "id= " + id +
        " , start=" + start +
        " , end=" + end +
        "}";
    }

    public static Builder builder(Date start) {
        return new Builder(start);
    }

    public static class Builder {
        private Date start;
        private Date end;
        private Journey journey;

        public Builder(Date start) {
            this.start = start;
        }

        public Builder end(Date end) {
            this.end = end;
            return this;
        }

        public Builder journey(Journey journey) {
            this.journey = journey;
            return this;
        }

        public Itinerary build() {
            Itinerary it = new Itinerary();
            it.setStart(this.start);
            it.setEnd(this.end);
            it.setJourney(this.journey);
            return it;
        }
    }
}