package com.journey.domain.itinerary;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ItineraryValidator implements ConstraintValidator<ValidItinerary, Itinerary> {
    @Override
    public void initialize (ValidItinerary constraintAnnotation) {
    }

    @Override
    public boolean isValid (Itinerary it, ConstraintValidatorContext context) {
        if (it.getEnd() != null) {
            return it.getStart().equals(it.getEnd()) 
              || it.getStart().before(it.getEnd());
        }
        return true;
    }
}