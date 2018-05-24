package com.journey.domain.itinerary;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ItineraryValidator.class)
@Documented
public @interface ValidItinerary {
    String message () default "start date must be earlier than end date. " +
                        "Found: ${formatter.format('%1$tb %1$te %1$tY', validatedValue.start)} < ${formatter.format('%1$tb %1$te %1$tY', validatedValue.end)}";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}