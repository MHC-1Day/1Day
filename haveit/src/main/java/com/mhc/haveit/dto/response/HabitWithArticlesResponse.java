package com.mhc.haveit.dto.response;

import com.mhc.haveit.dto.HabitWithArticlesDto;
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
        String nickname = dto.getUserAccountDto().getNickname();

        if(nickname==null || nickname.isBlank()){
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
                .articleWithCommentResponses(dto.getArticleWithCommentDtos().stream()
                        .map(ArticleWithCommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .build();
    }
}
