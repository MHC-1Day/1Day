package com.mhc.haveit.config;

import com.mhc.haveit.dto.security.HavitPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())    // 인증 정보를 전부 가지고있는 클래스
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)    // 인증이 됐는지
                .map(Authentication::getPrincipal)
                .map(HavitPrincipal.class::cast)    // ( x -> (HavitPrincipal) x ) 타입 캐스팅 역할
                .map(HavitPrincipal::getUsername);
    }

}
