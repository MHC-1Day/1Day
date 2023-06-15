package com.mhc.haveit.dto;

import com.mhc.haveit.domain.Habit;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HabitDto implements Serializable {

    private Long id;
    private String name;
    private UserAccountDto userAccountDto;
    private ArticleDto articleDto;
    private String content;
    private String hashtag;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static HabitDto from(Habit entity){
        return HabitDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .userAccountDto(UserAccountDto.from(entity.getUserAccount()))
                .content(entity.getContent())
                .hashtag(entity.getHashtag())
                .endDate(entity.getEndDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }

    public Habit toEntity() {
        return Habit.builder()
                .id(id)
                .name(name)
                .userAccount(userAccountDto.toEntity())
                .content(content)
                .hashtag(hashtag)
                .endDate(endDate)
                .build();
    }
}
