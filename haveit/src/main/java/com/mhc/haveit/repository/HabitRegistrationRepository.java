package com.mhc.haveit.repository;

import com.mhc.haveit.domain.HabitRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRegistrationRepository extends JpaRepository<HabitRegistration, Long> {
}
