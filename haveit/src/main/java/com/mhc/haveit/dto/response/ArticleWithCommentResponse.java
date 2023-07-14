package com.mhc.haveit.dto.response;

import com.mhc.haveit.dto.ArticleWithCommentDto;
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
public class ArticleWithCommentResponse{
    private Long id;
    private String title;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private Set<CommentResponse> commentResponses;

    public static ArticleWithCommentResponse from(ArticleWithCommentDto dto){
        Optional.ofNullable(dto).orElseThrow(()-> new IllegalArgumentException("잘못된 dto 입니다. - dto:"+dto));

        String nickname = dto.getUserAccountDto().getNickname();
        if(nickname.isBlank()){
            nickname = dto.getUserAccountDto().getUserId();
        }

        return ArticleWithCommentResponse.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .nickname(nickname)
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .commentResponses(
                        Optional.ofNullable(dto.getCommentDtos())
                                    .orElseThrow(()->new NullPointerException("dto 의 comments 가 null 입니다. "))
                                .stream()
                                .map(CommentResponse::from)
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .build();
    }


}
