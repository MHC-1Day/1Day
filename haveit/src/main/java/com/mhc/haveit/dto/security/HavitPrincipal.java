package com.mhc.haveit.dto.security;

import com.mhc.haveit.dto.UserAccountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class HavitPrincipal implements UserDetails {
    Long id;
    String userId;
    String password;
    Collection<? extends GrantedAuthority> authorities;
    String email;
    String nickname;
    String memo;

    public static HavitPrincipal of(Long id, String userId, String password, String email, String nickname, String memo) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new HavitPrincipal(
                id,
                userId,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet())
                ,
                email,
                nickname,
                memo);
    }

    public static HavitPrincipal from(UserAccountDto dto){
        return HavitPrincipal.of(
                dto.getId(),
                dto.getUserId(),
                dto.getUserPassword(),
                dto.getEmail(),
                dto.getNickname(),
                dto.getMemo()
        );
    }

    public UserAccountDto toDto(){
        return UserAccountDto.builder()
                .id(id)
                .userId(userId)
                .userPassword(password)
                .email(email)
                .nickname(nickname)
                .memo(memo)
                .build();
    }

    @Override public String getUsername() { return userId; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return null;}

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public enum RoleType{
        USER("ROLE_USER");  // spring security 권한 표현 규칙

        @Getter
        private final String name;

        RoleType(String name){this.name=name;}
    }
}
