package com.mhc.haveit.repository;
import com.mhc.haveit.config.JpaConfig;
import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final UserAccountRepository userAccountRepository;
    private final HabitRepository habitRepository;

    public JpaRepositoryTest(
            @Autowired UserAccountRepository userAccountRepository,
            @Autowired HabitRepository habitRepository
    ) {
        this.userAccountRepository = userAccountRepository;
        this.habitRepository = habitRepository;
    }

    @DisplayName("[SELECT] 테스트")
    @Test
    void givenTestData_whenSelecting_thenFine() {
        // Given

        // When
        List<UserAccount> userAccounts = userAccountRepository.findAll();
        List<Habit> habits = habitRepository.findAll();

        // Then
        assertThat(userAccounts)
                .isNotNull()
                .hasSize(2);
        assertThat(habits)
                .isNotNull()
                .hasSize(100);
    }

    @DisplayName("[INSERT] 테스트")
    @Test
    void givenTestData_whenInserting_thenFine() {
        // Given
        long previousCount = userAccountRepository.count();

        // When
        userAccountRepository.save(
                UserAccount.builder()
                        .userId("jsh")
                        .userPassword("pw")
                        .nickname("Jeong")
                        .email("jsh@mail.com")
                        .memo("memo")
                        .build()
        );

        // Then
        assertThat(userAccountRepository.count())
                .isEqualTo(previousCount+1);
    }

    @DisplayName("[UPDATE] 테스트")
    @Test
    void givenTestData_whenUpdating_thenFine() {
        // Given
        UserAccount userAccount = userAccountRepository.findById(1L).orElseThrow();
        String updateMemo = "Haveit";
        userAccount.setMemo(updateMemo);

        // When
        UserAccount updateUserAccount = userAccountRepository.save(userAccount);

        // Then
        assertThat(updateUserAccount)
                .hasFieldOrPropertyWithValue("memo",updateMemo);
    }

    @DisplayName("[DELETE] 테스트")
    @Test
    void givenTestData_whenDeleting_thenFine() {
        // Given
        long previousCount = habitRepository.count();

        // When
        habitRepository.deleteById(1L);

        // Then
        assertThat(habitRepository.count())
                .isEqualTo(previousCount-1);
    }

    /*
        Jpa 의 auditorAware 기능 때문에 insert 에 null 값이 들어가는 문제가 생김.
        Security 와는 무관하게 테스트 하기위해 TestConfig 를 추가 함
     */
    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig {
        @Bean
        public AuditorAware<String> auditorAware(){
            return () -> Optional.of("jsh");
        }
    }

}
