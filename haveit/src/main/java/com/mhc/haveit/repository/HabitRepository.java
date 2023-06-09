package com.mhc.haveit.repository;

import com.mhc.haveit.domain.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface HabitRepository extends JpaRepository<Habit, Long>{
}
