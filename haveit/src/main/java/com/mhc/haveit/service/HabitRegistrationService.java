package com.mhc.haveit.service;

import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.UserAccount;
import com.mhc.haveit.dto.HabitRegistrationDto;
import com.mhc.haveit.repository.HabitRegistrationRepository;
import com.mhc.haveit.repository.HabitRepository;
import com.mhc.haveit.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HabitRegistrationService {
    private final HabitRegistrationRepository habitRegistrationRepository;
    private final HabitRepository habitRepository;
    private final UserAccountRepository userAccountRepository;
    public void saveHabitRegistration(HabitRegistrationDto dto) {

        if(Objects.isNull(dto.getHabitId())){
            throw new IllegalArgumentException("선택한 습관이 없습니다.");
        }
        Habit habit = habitRepository.getReferenceById(dto.getHabitId());

        UserAccount userAccount = userAccountRepository.findByUserId(dto.getUserAccountId())
                .orElseThrow(()->new UsernameNotFoundException("존재하지않는 유저입니다."));

        boolean isExist = habitRegistrationRepository.existsHabitRegistrationByHabit_IdAndUserAccount_UserId(habit.getId(), userAccount.getUserId());
        if(isExist){
            log.warn("이미 신청된 습관입니다.");
            return;
        }
        habitRegistrationRepository.save(dto.toEntity(habit,userAccount));
    }

    public void deleteHabitRegistration(Long habitId, String userId) {
        habitRegistrationRepository.deleteByHabit_IdAndUserAccount_UserId(habitId,userId);
    }

    public List<HabitRegistrationDto> findByUserId(String userId) {
        return habitRegistrationRepository.findByUserAccount_UserId(userId).stream()
                .map(HabitRegistrationDto::from)
                .toList();

    }

    public List<HabitRegistrationDto> findByHabitId(Long habitId) {
        return habitRegistrationRepository.findByHabit_Id(habitId).stream()
                .map(HabitRegistrationDto::from)
                .toList();
    }

    public boolean isExistHabitRegistration(Long habitId, String userId){
        return habitRegistrationRepository.existsHabitRegistrationByHabit_IdAndUserAccount_UserId(habitId,userId);
    }

    public Long countByUserId(String userId) {
        return habitRegistrationRepository.countByUserAccount_UserId(userId);
    }

    public Long countByHabitId(Long habitId) {
        return habitRegistrationRepository.countByHabit_Id(habitId);
    }
}
