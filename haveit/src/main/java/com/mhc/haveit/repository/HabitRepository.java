package com.mhc.haveit.repository;

import com.mhc.haveit.domain.Habit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface HabitRepository extends JpaRepository<Habit, Long>{
    Page<Habit> findByNameContaining(String keyword, Pageable pageable);
    Page<Habit> findByUserAccount_UserIdContaining(String keyword, Pageable pageable);
    Page<Habit> findByUserAccount_NicknameContaining(String keyword, Pageable pageable);
    Page<Habit> findByHashtagContaining(String keyword, Pageable pageable);
}
