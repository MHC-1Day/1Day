package com.mhc.haveit.service;

import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.type.SearchType;
import com.mhc.haveit.dto.HabitDto;
import com.mhc.haveit.dto.HabitWithArticlesDto;
import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.repository.HabitRepository;
import com.mhc.haveit.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HabitService {

    private final HabitRepository habitRepository;

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
        habitRepository.save(dto.toEntity());
    }

    public void updateHabit(Long habitId, HabitDto dto) {
        Optional<Habit> habit = habitRepository.findById(habitId);
        habit.orElseThrow(()-> new EntityNotFoundException("습관 업데이트 실패. 습관을 찾을 수 없습니다 - dto: "+dto));

        if(dto.getName() !=null){ habit.get().setName(dto.getName()); }
        if(dto.getContent() != null){ habit.get().setContent(dto.getContent()); }
        if(dto.getHashtag() != null){ habit.get().setHashtag(dto.getHashtag()); }
        if(dto.getEndDate() != null){ habit.get().setEndDate(dto.getEndDate()); }
    }

    public void deleteHabit(Long habitId) {
        habitRepository.deleteById(habitId);
    }

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
