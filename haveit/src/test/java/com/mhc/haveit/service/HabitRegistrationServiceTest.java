package com.mhc.haveit.service;

import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.HabitRegistration;
import com.mhc.haveit.domain.UserAccount;
import com.mhc.haveit.dto.HabitDto;
import com.mhc.haveit.dto.HabitRegistrationDto;
import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.repository.HabitRegistrationRepository;
import com.mhc.haveit.repository.HabitRepository;
import com.mhc.haveit.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 습관 신청")
@ExtendWith(MockitoExtension.class)
class HabitRegistrationServiceTest {
    @InjectMocks private HabitRegistrationService sut;

    @Mock private HabitRegistrationRepository habitRegistrationRepository;
    @Mock private HabitRepository habitRepository;
    @Mock private UserAccountRepository userAccountRepository;

    @DisplayName("습관 신청 정보를 입력하면, 습관 내역을 저장한다.")
    @Test
    void givenHabitRegistrationInfo_whenSaving_thenSavesHabitRegistration() {
        // Given
        HabitRegistrationDto habitRegistrationDto = createHabitRegistrationDto(createHabitDto(),createUserAccountDto());
        given(userAccountRepository.findByUserId(anyString())).willReturn(Optional.of(createUserAccount()));
        given(habitRepository.getReferenceById(1L)).willReturn(createHabit());
        given(habitRegistrationRepository.save(any(HabitRegistration.class))).willReturn(null);

        // When
        sut.saveHabitRegistration(habitRegistrationDto);

        // Then
        then(userAccountRepository).should().findByUserId(anyString());
        then(habitRepository).should().getReferenceById(1L);
        then(habitRegistrationRepository).should().save(any(HabitRegistration.class));
    }

    @DisplayName("유저 ID 와 습관 ID 주면, 습관 내역을 삭제한다.")
    @Test
    void givenHabitIdAndUserAccountId_whenDeleting_thenDeletesHabitRegistration() {
        // Given
        String userId = "userId";
        Long habitId = 1L;
        willDoNothing().given(habitRegistrationRepository).deleteByHabit_IdAndUserAccount_UserId(any(Long.class),any(String.class));

        // When
        sut.deleteHabitRegistration(habitId,userId);

        // Then
        then(habitRegistrationRepository).should().deleteByHabit_IdAndUserAccount_UserId(habitId,userId);

    }

    @DisplayName("유저 ID 로 조회하면, 신청한 습관들을 조회한다.")
    @Test
    void givenUserAccountId_whenFinding_thenFindsHabits() {
        // Given
        String userId = "userId";
        given(habitRegistrationRepository.findByUserAccount_UserId(userId)).willReturn(List.of());

        // When
        List<HabitRegistrationDto> dtos = sut.findByUserId(userId);

        // Then
        then(habitRegistrationRepository).should().findByUserAccount_UserId(userId);
    }

    @DisplayName("습관 ID 로 조회하면, 신청한 유저들을 조회한다.")
    @Test
    void givenHabitId_whenFinding_thenFindsUserAccounts() {
        // Given
        Long habitId = 1L;
        given(habitRegistrationRepository.findByHabit_Id(habitId)).willReturn(List.of());

        // When
        List<HabitRegistrationDto> dtos = sut.findByHabitId(habitId);

        // Then
        then(habitRegistrationRepository).should().findByHabit_Id(habitId);

    }

    @DisplayName("유저 ID 로 조회하면, 신청한 습관 수를 조회한다.")
    @Test
    void givenUserAccountId_whenCounting_thenCountsHabit() {
        // Given
        String userId = "userId";
        Long expected = 1L;
        given(habitRegistrationRepository.countByUserAccount_UserId(userId)).willReturn(expected);

        // When
        Long actual = sut.countByUserId(userId);

        // Then
        assertThat(actual).isEqualTo(expected);
        then(habitRegistrationRepository).should().countByUserAccount_UserId(userId);
    }

    @DisplayName("습관 ID 로 조회하면, 신청한 유저 수를 조회한다.")
    @Test
    void givenHabitId_whenCounting_thenCountsUserAccount() {
        // Given
        Long habitId = 1L;
        Long expected = 1L;
        given(habitRegistrationRepository.countByHabit_Id(habitId)).willReturn(expected);

        // When
        Long actual = sut.countByHabitId(habitId);

        // Then
        assertThat(actual).isEqualTo(expected);
        then(habitRegistrationRepository).should().countByHabit_Id(habitId);
    }

    @DisplayName("습관 ID 와 유저 ID 로 조회하면, 신청 여부를 조회한다.")
    @Test
    void givenHabitIdAndUserAccountId_whenChecking_thenReturnsIsExists(){
        // Given
        Long habitId = 1L;
        String userId = "userId";
        boolean expect = true;
        given(habitRegistrationRepository.existsHabitRegistrationByHabit_IdAndUserAccount_UserId(habitId,userId)).willReturn(expect);

        // When
        boolean actual = sut.isExistHabitRegistration(habitId,userId);

        // Then
        assertThat(actual).isEqualTo(expect);
        then(habitRegistrationRepository).should().existsHabitRegistrationByHabit_IdAndUserAccount_UserId(habitId,userId);
    }

    private HabitRegistrationDto createHabitRegistrationDto(HabitDto habitDto, UserAccountDto userAccountDto) {
        return HabitRegistrationDto.builder()
                .id(1L)
                .habitId(habitDto.getId())
                .userAccountId(userAccountDto.getUserId())
                .build();
    }

    private Habit createHabit(){
        return Habit.builder()
                .id(1L)
                .name("Spring")
                .userAccount(createUserAccount())
                .content("study")
                .hashtag("#java")
                .build();
    }

    private HabitDto createHabitDto(){
        return HabitDto.builder()
                .id(1L)
                .name("Spring")
                .userAccountDto(createUserAccountDto())
                .content("study")
                .hashtag("#java")
                .createdAt(LocalDateTime.now())
                .createdBy("jsh")
                .modifiedAt(LocalDateTime.now())
                .modifiedBy("jsh")
                .build();
    }

    private UserAccount createUserAccount(){
        return UserAccount.builder()
                .id(1L)
                .nickname("Jeong")
                .email("jsh@mail.com")
                .memo("memo")
                .build();
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.builder()
                .id(1L)
                .userId("jsh")
                .nickname("Jeong")
                .email("jsh@mail.com")
                .memo("memo")
                .createdAt(LocalDateTime.now())
                .createdBy("jsh")
                .modifiedAt(LocalDateTime.now())
                .modifiedBy("jsh")
                .build();
    }
}