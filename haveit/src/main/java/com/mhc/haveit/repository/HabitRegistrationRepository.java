package com.mhc.haveit.repository;

import com.mhc.haveit.domain.HabitRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface HabitRegistrationRepository extends JpaRepository<HabitRegistration, Long> {
}
