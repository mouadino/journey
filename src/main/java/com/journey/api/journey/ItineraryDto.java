package com.journey.api.journey;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

class ItineraryDto {
    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm'Z'")
    private Date start;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm'Z'")
    private Date end;
 
    public Date getStart(){
       return start;
    }
 
    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd(){
        return end;
     }
  
     public void setEnd(Date end) {
         this.end = end;
         
     }
}