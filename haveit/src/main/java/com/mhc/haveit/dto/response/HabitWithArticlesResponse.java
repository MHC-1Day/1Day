package com.mhc.haveit.dto.response;

import com.mhc.haveit.dto.HabitWithArticlesDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HabitWithArticlesResponse {
    private Long id;
    private String name;
    private String content;
    private String hashtag;
    private String nickname;
    private String email;
    private LocalDateTime endDate;
    private LocalDateTime createdAt;
    private Set<ArticleWithCommentResponse> articleWithCommentResponses;

    public static HabitWithArticlesResponse from(HabitWithArticlesDto dto) {
        Optional.ofNullable(dto).orElseThrow(()->new IllegalArgumentException("dto -> response : dto 가 Null 입니다"));

        String nickname = dto.getUserAccountDto().getNickname();
        if(nickname.isBlank()){
            nickname = dto.getUserAccountDto().getUserId();
        }

        return HabitWithArticlesResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .content(dto.getContent())
                .hashtag(dto.getHashtag())
                .nickname(nickname)
                .email(dto.getUserAccountDto().getEmail())
                .endDate(dto.getEndDate())
                .createdAt(dto.getCreatedAt())
                .articleWithCommentResponses(
                        Optional.ofNullable(dto.getArticleWithCommentDtos())
                                    .orElseThrow(()->new NullPointerException("dto 의 articles 가 null 입니다. "))
                                .stream()
                                .map(ArticleWithCommentResponse::from)
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .build();
    }
}
