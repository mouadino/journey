package com.journey.domain.journey;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JourneyRepository extends PagingAndSortingRepository<Journey, Long> {
}