package com.mhc.haveit.dto.request;

import com.mhc.haveit.dto.HabitDto;
import com.mhc.haveit.dto.UserAccountDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitRequest {
    private String name;
    private String content;
    private String hashtag;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public HabitDto toDto(UserAccountDto userAccountDto){
        return HabitDto.builder()
                .name(name)
                .userAccountDto(userAccountDto)
                .content(content)
                .hashtag(hashtag)
                .endDate(endDate.atStartOfDay())
                .build();
    }

}
