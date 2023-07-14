package com.mhc.haveit.dto;

import com.mhc.haveit.domain.Article;
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
public class ArticleWithCommentDto {
    private Long id;
    private UserAccountDto userAccountDto;
    private String title;
    private String content;
    private Set<CommentDto> commentDtos;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static ArticleWithCommentDto from(Article entity){
        Optional.ofNullable(entity).orElseThrow(()-> new IllegalArgumentException("잘못된 entity 입니다. - entity:"+entity));
        return ArticleWithCommentDto.builder()
                .id(entity.getId())
                .userAccountDto(UserAccountDto.from(entity.getUserAccount()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .commentDtos(
                        Optional.ofNullable(entity.getComments())
                                    .orElseThrow(()->new NullPointerException("entity 의 comments 가 null 입니다. "))
                                .stream()
                                .map(CommentDto::from)
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                )
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }


}
