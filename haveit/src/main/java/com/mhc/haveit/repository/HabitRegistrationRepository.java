package com.mhc.haveit.repository;

import com.mhc.haveit.domain.HabitRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface HabitRegistrationRepository extends JpaRepository<HabitRegistration, Long> {
    void deleteByHabit_IdAndUserAccount_UserId(Long habitId, String userId);
    List<HabitRegistration> findByUserAccount_UserId(String userId);
    List<HabitRegistration> findByHabit_Id(Long habitId);
    Long countByHabit_Id(Long habitId);
    Long countByUserAccount_UserId(String userId);
    boolean existsHabitRegistrationByHabit_IdAndUserAccount_UserId(Long habitId, String userId);

}
