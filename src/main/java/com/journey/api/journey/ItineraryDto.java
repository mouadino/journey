package com.journey.api.journey;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
class ItineraryDto {
    private long id;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm'Z'")
    private Date start;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm'Z'")
    private Date end;
}