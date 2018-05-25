package com.journey.api.journey;

import static org.junit.Assert.assertThat;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

public class JourneyDtoValidationTest {

    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void JourneyWithNullName_shouldNotBeValid() {
        JourneyDto j = new JourneyDto();
        j.setName(null);

        Set<ConstraintViolation<JourneyDto>> violations = validator.validate(j);
        assertThat(violations, Matchers.not(Matchers.empty()));
    }

    @Test
    public void JourneyWithEmptyName_shouldNotBeValid() {
        JourneyDto j = new JourneyDto();
        j.setName("");

        Set<ConstraintViolation<JourneyDto>> violations = validator.validate(j);
        assertThat(violations, Matchers.not(Matchers.empty()));
    }

    @Test
    public void JourneyWithNoneEmptyName_shouldBeValid() {
        JourneyDto j = new JourneyDto();
        j.setName("foobar");

        Set<ConstraintViolation<JourneyDto>> violations = validator.validate(j);
        assertThat(violations, Matchers.empty());
    }
}