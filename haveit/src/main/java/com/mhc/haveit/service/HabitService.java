package com.mhc.haveit.service;

import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.UserAccount;
import com.mhc.haveit.domain.type.SearchType;
import com.mhc.haveit.dto.HabitDto;
import com.mhc.haveit.dto.HabitRegistrationDto;
import com.mhc.haveit.dto.HabitWithArticlesDto;
import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.repository.HabitRegistrationRepository;
import com.mhc.haveit.repository.HabitRepository;
import com.mhc.haveit.repository.UserAccountRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HabitService {
    private final UserAccountRepository userAccountRepository;

    private final HabitRepository habitRepository;
    private final HabitRegistrationRepository habitRegistrationRepository;

    @Transactional(readOnly = true)
    public Page<HabitDto> searchHabits(SearchType searchType, String searchKeyword, Pageable pageable){
        if(searchKeyword == null || searchKeyword.isBlank()){
            return habitRepository.findAll(pageable).map(HabitDto::from);
        }
        return switch (searchType){
            case TITLE ->  habitRepository.findByNameContaining(searchKeyword,pageable).map(HabitDto::from);
            case ID -> habitRepository.findByUserAccount_UserIdContaining(searchKeyword,pageable).map(HabitDto::from);
            case NICKNAME -> habitRepository.findByUserAccount_NicknameContaining(searchKeyword,pageable).map(HabitDto::from);
            case HASHTAG -> habitRepository.findByHashtagContaining(searchKeyword,pageable).map(HabitDto::from);
        };
    }

    @Transactional(readOnly = true)
    public HabitDto getHabit(Long habitId) {
        return habitRepository.findById(habitId)
                .map(HabitDto::from)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 습관입니다. - habitId:"+habitId));
    }

    public void saveHabit(HabitDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getId());
        HabitRegistrationDto habitRegistrationDto = HabitRegistrationDto.builder()
                                                                        .userAccountId(userAccount.getUserId())
                                                                        .habitId(dto.getId())
                                                                        .build();
        Habit habitEntity = dto.toEntity(userAccount);
        habitRepository.save(habitEntity);

        habitRegistrationRepository.save(habitRegistrationDto.toEntity(habitEntity,userAccount));
    }

    public void updateHabit(Long habitId, HabitDto dto) {
        try {
            Habit habit = habitRepository.getReferenceById(habitId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getId());

            if (Objects.equals(habit.getUserAccount().getId(), userAccount.getId())) {
                if (dto.getName() != null) { habit.setName(dto.getName()); }
                if (dto.getContent() != null) { habit.setContent(dto.getContent()); }
                if (dto.getHashtag() != null) { habit.setHashtag(dto.getHashtag()); }
                if (dto.getEndDate() != null) { habit.setEndDate(dto.getEndDate()); }
            }
        } catch (EntityNotFoundException e){
            log.warn("습관 업데이트 실패. 습관을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}",e.getLocalizedMessage());
        }
    }

    public void deleteHabit(Long habitId, String userId) {
        habitRepository.deleteByIdAndUserAccount_UserId(habitId,userId);
    }

    @Transactional(readOnly = true)
    public Long getHabitCount() {
        return habitRepository.count();
    }

    @Transactional(readOnly = true)
    public HabitWithArticlesDto getHabitWithArticles(Long habitId) {
        return habitRepository.findById(habitId)
                .map(HabitWithArticlesDto::from)
                .orElseThrow(() -> new EntityNotFoundException("습관이 없습니다 - habitId:"+habitId));
    }
}
