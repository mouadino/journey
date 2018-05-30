package com.journey.domain.itinerary;

import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.journey.domain.journey.Journey;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class ItineraryValidationTest {

    private Journey dummyJourney;
    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        dummyJourney = new Journey();
    }

    @Test
    public void ItineraryWithStartDateEqualsToEndDate_shouldBeValid() {
        Date now = new Date();
        Itinerary it = Itinerary.builder()
                                .start(now)
                                .end(now)
                                .journey(dummyJourney)
                                .build();

        Set<ConstraintViolation<Itinerary>> violations = validator.validate(it);
        assertThat(violations, Matchers.empty());
    }

    @Test
    public void ItineraryWithNoEndDate_shouldBeValid() {
        Date now = new Date();
        Itinerary it = Itinerary.builder()
                                .start(now)
                                .journey(dummyJourney)
                                .build();

        Set<ConstraintViolation<Itinerary>> violations = validator.validate(it);
        assertThat(violations, Matchers.empty());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void ItineraryWithStartDateLessThanEndDate_shouldBeValid() {
        Itinerary it = Itinerary.builder()
                                .start(new Date(2018, 5, 22))
                                .end(new Date(2018, 5, 24))
                                .journey(dummyJourney)
                                .build();

        Set<ConstraintViolation<Itinerary>> violations = validator.validate(it);
        assertThat(violations, Matchers.empty());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void ItineraryWithStartDateBiggerThanEndDate_shouldNotBeValid() {
        Itinerary it = Itinerary.builder().build();
        it.setStart(new Date(2018, 5, 31));
        it.setEnd(new Date(2018, 5, 24));

        Set<ConstraintViolation<Itinerary>> violations = validator.validate(it);
        assertThat(violations, Matchers.not(Matchers.empty()));
    }
}