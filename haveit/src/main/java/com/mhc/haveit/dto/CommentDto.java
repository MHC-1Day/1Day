package com.mhc.haveit.dto;

import com.mhc.haveit.domain.Article;
import com.mhc.haveit.domain.Comment;
import com.mhc.haveit.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long articleId;
    private UserAccountDto userAccountDto;
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public Comment toEntity(Article article, UserAccount userAccount) {
        return Comment.builder()
                .id(id)
                .article(article)
                .userAccount(userAccount)
                .content(content)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .modifiedAt(modifiedAt)
                .modifiedBy(modifiedBy)
                .build();
    }

    public static CommentDto from(Comment entity) {
        return CommentDto.builder()
                .id(entity.getId())
                .articleId(entity.getArticle().getId())
                .userAccountDto(UserAccountDto.from(entity.getUserAccount()))
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}
