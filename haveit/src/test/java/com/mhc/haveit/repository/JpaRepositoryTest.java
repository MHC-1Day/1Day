package com.mhc.haveit.repository;
import com.mhc.haveit.config.JpaConfig;
import com.mhc.haveit.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Import(JpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final UserAccountRepository userAccountRepository;

    public JpaRepositoryTest(@Autowired UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("[SELECT] 테스트")
    @Test
    void givenTestData_whenSelecting_thenFine() {
        // Given

        // When
        List<UserAccount> userAccounts = userAccountRepository.findAll();

        // Then
        assertThat(userAccounts)
                .isNotNull()
                .hasSize(1);
    }

    @DisplayName("[INSERT] 테스트")
    @Test
    void givenTestData_whenInserting_thenFine() {
        // Given
        long previousCount = userAccountRepository.count();

        // When
        userAccountRepository.save(UserAccount.of("jsh2","pw","Jeong","jsh@email.com","memo"));

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
        UserAccount userAccount = userAccountRepository.findById(1L).orElseThrow();
        long previousCount = userAccountRepository.count();

        // When
        userAccountRepository.delete(userAccount);

        // Then
        assertThat(userAccountRepository.count())
                .isEqualTo(previousCount-1);
    }
}