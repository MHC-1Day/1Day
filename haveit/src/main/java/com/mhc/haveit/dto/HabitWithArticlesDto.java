package com.mhc.haveit.dto;

import com.mhc.haveit.domain.Habit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitWithArticlesDto{
    private Long id;
    private UserAccountDto userAccountDto;
    private Set<ArticleDto> articleDtos;
    private String name;
    private String content;
    private String hashtag;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static HabitWithArticlesDto from(Habit entity) {
        return HabitWithArticlesDto.builder()
                .id(entity.getId())
                .userAccountDto(UserAccountDto.from(entity.getUserAccount()))
                .articleDtos(entity.getArticles().stream()
                        .map(ArticleDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .name(entity.getName())
                .content(entity.getContent())
                .hashtag(entity.getHashtag())
                .endDate(entity.getEndDate())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}
