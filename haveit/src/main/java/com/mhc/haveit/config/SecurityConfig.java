package com.mhc.haveit.config;

import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.dto.security.HavitPrincipal;
import com.mhc.haveit.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                PathRequest.toStaticResources().atCommonLocations()
                        ).permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/",
                                "/habits"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login.defaultSuccessUrl("/",true))
                .logout(logout -> logout.logoutSuccessUrl("/"))
                .build();
    }
/*
    spring security 검사에서 제외
    static method ( css, js ..)

    security 에 관한 모든 서비스를 이용하지 않아 추천하지 않는다고 함
    csrf 같은 보안 공격에 취약해 질 수 있어서 다른 방식을 권장 함

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
*/
    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository){
        return userId -> userAccountRepository
                .findByUserId(userId)
                .map(UserAccountDto::from)
                .map(HavitPrincipal::from)
                .orElseThrow(()-> new UsernameNotFoundException("유저를 찾을 수 없습니다 - userId: "+userId));
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
