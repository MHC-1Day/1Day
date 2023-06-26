package com.mhc.haveit.dto.response;

import com.mhc.haveit.dto.HabitDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitResponse{
    private Long id;
    private String name;
    private String content;
    private String hashtag;
    private String nickname;
    private LocalDate endDate;
    private LocalDateTime createdAt;

    public static HabitResponse from(HabitDto dto) {
        String nickname = dto.getUserAccountDto().getNickname();

        if(nickname==null || nickname.isBlank()){
            nickname = dto.getUserAccountDto().getUserId();
        }

        return HabitResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .content(dto.getContent())
                .hashtag(dto.getHashtag())
                .nickname(nickname)
                .endDate(dto.getEndDate().toLocalDate())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
