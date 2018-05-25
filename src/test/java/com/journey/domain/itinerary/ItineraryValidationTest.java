package com.journey.domain.itinerary;

import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class ItineraryValidationTest {

    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void ItineraryWithStartDateEqualsToEndDate_shouldBeValid() {
        Date now = new Date();
        Itinerary it = new Itinerary();
        it.setStart(now);
        it.setEnd(now);

        Set<ConstraintViolation<Itinerary>> violations = validator.validate(it);
        assertThat(violations, Matchers.empty());
    }

    @Test
    public void ItineraryWithNoEndDate_shouldBeValid() {
        Itinerary it = new Itinerary();
        it.setStart(new Date());

        Set<ConstraintViolation<Itinerary>> violations = validator.validate(it);
        assertThat(violations, Matchers.empty());
    }

    @Test
    public void ItineraryWithStartDateLessThanEndDate_shouldBeValid() {
        Itinerary it = new Itinerary();
        it.setStart(new Date(2018, 5, 22));
        it.setEnd(new Date(2018, 5, 24));

        Set<ConstraintViolation<Itinerary>> violations = validator.validate(it);
        assertThat(violations, Matchers.empty());
    }

    @Test
    public void ItineraryWithStartDateBiggerThanEndDate_shouldNotBeValid() {
        Itinerary it = new Itinerary();
        it.setStart(new Date(2018, 5, 31));
        it.setEnd(new Date(2018, 5, 24));

        Set<ConstraintViolation<Itinerary>> violations = validator.validate(it);
        assertThat(violations, Matchers.not(Matchers.empty()));
    }
}