package com.mhc.haveit.service;

import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.HabitRegistration;
import com.mhc.haveit.domain.UserAccount;
import com.mhc.haveit.domain.type.SearchType;
import com.mhc.haveit.dto.HabitDto;
import com.mhc.haveit.dto.HabitWithArticlesDto;
import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.repository.HabitRegistrationRepository;
import com.mhc.haveit.repository.HabitRepository;
import com.mhc.haveit.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 습관")
@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @InjectMocks private HabitService sut;
    @Mock private HabitRepository habitRepository;
    @Mock private UserAccountRepository userAccountRepository;
    @Mock private HabitRegistrationRepository habitRegistrationRepository;

    @DisplayName("검색어 없이 습관목록을 검색하면, 습관페이지를 반환한다.")
    @Test
    void givenNothing_whenSearchingHabits_thenReturnsHabitsPage(){
        // Given
        Pageable pageable = Pageable.ofSize(12);
        given(habitRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<HabitDto> habits = sut.searchHabits(null,null,pageable);

        // Then
        assertThat(habits).isEqualTo(Page.empty());
        then(habitRepository).should().findAll(pageable);
    }

    @DisplayName("검색어와 함께 습관목록을 검색하면, 습관페이지를 반환한다.")
    @Test
    void givenSearchKeyword_whenSearchingHabits_thenReturnsHabitsPage(){
        // Given
        SearchType searchType = SearchType.ID;
        String searchKeyword = "userId";
        Pageable pageable = Pageable.ofSize(12);
        given(habitRepository.findByUserAccount_UserIdContaining(searchKeyword,pageable)).willReturn(Page.empty());

        // When
        Page<HabitDto> habits = sut.searchHabits(searchType,searchKeyword,pageable);

        // Then
        assertThat(habits).isEqualTo(Page.empty());
        then(habitRepository).should().findByUserAccount_UserIdContaining(searchKeyword,pageable);
    }

    @DisplayName("습관을 조회하면, 습관 상세페이지를 반환한다.")
    @Test
    void givenHabitId_whenSearchingHabit_thenReturnsHabit(){
        // Given
        Long habitId = 1L;
        Habit habit = createHabit();
        given(habitRepository.findById(habitId)).willReturn(Optional.of(habit));

        // When
        HabitDto dto = sut.getHabit(habitId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("name",habit.getName())
                .hasFieldOrPropertyWithValue("content", habit.getContent())
                .hasFieldOrPropertyWithValue("hashtag",habit.getHashtag());
        then(habitRepository).should().findById(habitId);
    }

    @DisplayName("없는 습관을 조회하면, 예외를 던진다.")
    @Test
    void givenNonexistentHabitId_whenSearchingHabit_thenThrowsException(){
        // Given
        Long habitId = 0L;

        // When
        Throwable t = catchThrowable(()-> sut.getHabit(habitId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("존재하지 않는 습관입니다. - habitId:"+habitId);
        then(habitRepository).should().findById(habitId);
    }

    @DisplayName("습관 정보를 입력하면, 습관을 생성한다.")
    @Test
    void givenHabitInfo_whenSavingHabit_thenSavesHabit(){
        // Given
        HabitDto habitDto = createHabitDto("study","content","java");
        given(habitRepository.save(any(Habit.class))).willReturn(createHabit());
        given(userAccountRepository.getReferenceById(anyLong())).willReturn(createuserAccount());
        given(habitRegistrationRepository.save(any(HabitRegistration.class))).willReturn(any(HabitRegistration.class));

        // When
        sut.saveHabit(habitDto);

        // Then
        then(habitRepository).should().save(any(Habit.class));
        then(userAccountRepository).should().getReferenceById(anyLong());
        then(habitRegistrationRepository).should().save(any(HabitRegistration.class));
    }

    @DisplayName("습관의 수정 정보를 입력하면, 습관을 수정한다.")
    @Test
    void givenModifiedHabitInfo_whenUpdatingHabit_thenUpdatesHabit(){
        // Given
        Habit habit = createHabit();
        HabitDto dto = createHabitDto("walk","hardWalking","#health");
        given(habitRepository.getReferenceById(dto.getId())).willReturn(habit);
        given(userAccountRepository.getReferenceById(dto.getUserAccountDto().getId())).willReturn(dto.getUserAccountDto().toEntity());

        // When
        sut.updateHabit(dto.getId(),dto);

        // Then
        assertThat(habit.getName()).isEqualTo("walk");
        assertThat(habit)
                .hasFieldOrPropertyWithValue("name",dto.getName())
                .hasFieldOrPropertyWithValue("content",dto.getContent())
                .hasFieldOrPropertyWithValue("hashtag",dto.getHashtag());
        then(habitRepository).should().getReferenceById(dto.getId());
        then(userAccountRepository).should().getReferenceById(dto.getUserAccountDto().getId());
    }

    @DisplayName("없는 습관의 수정 정보를 입력하면, 경고를 던지고 아무것도 하지않는다.")
    @Test
    void givenNonexistentHabitInfo_whenUpdatingHabit_thenThrowsExceptionAndDoseNothing(){
        // Given
        HabitDto dto = createHabitDto("walk","hardWalking","#health");
        given(habitRepository.getReferenceById(dto.getId())).willThrow(new EntityNotFoundException());

        // When
        sut.updateHabit(dto.getId(),dto);

        // Then
        then(habitRepository).should().getReferenceById(dto.getId());
    }

    @DisplayName("습관의 ID 를 입력하면, 습관을 삭제한다.")
    @Test
    void givenHabitId_whenDeletingHabit_thenDeletesHabit(){
        // Given
        Long habitId = 1L;
        String userId = "jsh";
        willDoNothing().given(habitRepository).deleteByIdAndUserAccount_UserId(habitId,userId);

        // When
        sut.deleteHabit(habitId,userId);

        // Then
        then(habitRepository).should().deleteByIdAndUserAccount_UserId(habitId,userId);
    }

    @DisplayName("습관의 수를 조회하면, 습관의 총 수를 반환한다.")
    @Test
    void givenNothing_whenCountingHabits_thenReturnsHabitCount(){
        // Given
        Long expected = 0L;
        given(habitRepository.count()).willReturn(expected);

        // When
        Long actual = sut.getHabitCount();

        // Then
        assertThat(actual).isEqualTo(expected);
        then(habitRepository).should().count();
    }

    @Test
    void givenHabitInfoButNotUserAccountInfo_whenCasting_thenThrowsException(){
        // Given
        Habit habit = Habit.builder()
                                .id(1L)
                                .name("habit")
                                .content("content")
                                .endDate(LocalDateTime.now())
                                .build();

        // When
        Throwable t = catchThrowable(()->
                HabitWithArticlesDto.from(habit)
        );

        // Then
        assertThat(t).isInstanceOf(IllegalArgumentException.class);
    }

    private HabitDto createHabitDto(String name, String content, String hashtag){
        return HabitDto.builder()
                .id(1L)
                .name(name)
                .userAccountDto(createUserAccountDto())
                .content(content)
                .hashtag(hashtag)
//                .createdAt(LocalDateTime.now())
//                .createdBy("jsh")
//                .modifiedAt(LocalDateTime.now())
//                .modifiedBy("jsh")
                .build();
    }
    private Habit createHabit(){
        return Habit.builder()
                .id(1L)
                .name("Spring")
                .userAccount(createuserAccount())
                .content("study")
                .hashtag("#java")
                .build();
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.from(createuserAccount());
//                .builder()
//                .id(1L)
//                .nickname("Jeong")
//                .email("jsh@mail.com")
//                .memo("memo")
//                .createdAt(LocalDateTime.now())
//                .createdBy("jsh")
//                .modifiedAt(LocalDateTime.now())
//                .modifiedBy("jsh")
//                .build();
    }

    private UserAccount createuserAccount(){
        return UserAccount.builder()
                .id(1L)
                .nickname("Jeong")
                .email("jsh@mail.com")
                .memo("memo")
                .build();
    }
}
