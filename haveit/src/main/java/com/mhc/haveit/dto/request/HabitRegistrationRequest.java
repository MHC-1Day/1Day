package com.mhc.haveit.dto.request;

import com.mhc.haveit.dto.HabitRegistrationDto;
import com.mhc.haveit.dto.UserAccountDto;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitRegistrationRequest {
    private Long habitId;

    public HabitRegistrationDto toDto(UserAccountDto userAccountDto){
        Optional.ofNullable(userAccountDto).orElseThrow(()->new IllegalArgumentException("null entity 입니다 "));

        return HabitRegistrationDto.builder()
                .habitId(habitId)
                .userAccountId(userAccountDto.getUserId())
                .build();
    }
}
