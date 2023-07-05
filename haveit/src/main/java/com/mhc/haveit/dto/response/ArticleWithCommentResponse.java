package com.mhc.haveit.dto.response;

import com.mhc.haveit.dto.ArticleWithCommentDto;
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
public class ArticleWithCommentResponse{
    private Long id;
    private String title;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private Set<CommentResponse> commentResponses;

    public static ArticleWithCommentResponse from(ArticleWithCommentDto dto){
        String nickname = dto.getUserAccountDto().getNickname();

        if(nickname==null || nickname.isBlank()){
            nickname = dto.getUserAccountDto().getUserId();
        }

        return ArticleWithCommentResponse.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .nickname(nickname)
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .commentResponses(dto.getCommentDtos().stream()
                        .map(CommentResponse::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .build();
    }


}
