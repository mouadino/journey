package com.journey.domain.itinerary;

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

import com.journey.domain.journey.Journey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(of = {"start", "end", "journey"}) @ToString @Builder
@ValidItinerary
@Entity
@Table(name = "itinerary")
public class Itinerary {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; 

    @Getter @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date start;

    @Getter @Setter
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")  // end is a keyword in PSQL.
    private Date end;

    @Getter @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journey_id")
    private Journey journey;
}