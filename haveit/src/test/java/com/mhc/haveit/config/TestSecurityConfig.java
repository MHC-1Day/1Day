package com.mhc.haveit.config;

import com.mhc.haveit.domain.UserAccount;
import com.mhc.haveit.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountRepository userAccountRepository;

    @BeforeTestMethod   // spring test 를 할 때 먼저 실행되게 함
    public void securitySetUp(){
        given(userAccountRepository.findByUserId(anyString())).willReturn(Optional.of(
                UserAccount.builder()
                        .userId("jshTest")
                        .userPassword("pw")
                        .email("jshTest@email.com")
                        .nickname("JeongTest")
                        .memo("test memo")
                        .build()

        ));
    }
}
