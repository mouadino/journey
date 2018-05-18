package com.journey.api.itinerary;

import java.util.Date;

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

import com.journey.api.journey.Journey;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "itinerary")
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; 

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @NotNull
    private Date start;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "endDate")  // end is a keyword in PSQL.
    private Date end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journey_id")
    private Journey journey;
    
    public Itinerary() {}

    public Itinerary(Date start, Date end) {
        this.start = start;
        this.end = end;
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

    public void setJourney(Journey j) {
        this.journey = j;
   }

    @Override
    public String toString() {
        return "Itinerary{" +
        "start=" + start +
        "end=" + end +
        "}";
    }
}