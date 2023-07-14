package com.mhc.haveit.dto;

import com.mhc.haveit.domain.Article;
import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.UserAccount;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto implements Serializable{
    private Long id;
    private Long habitId;
    private UserAccountDto userAccountDto;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static ArticleDto from(Article entity){
        Optional.ofNullable(entity).orElseThrow(()-> new IllegalArgumentException("잘못된 entity 입니다. - entity:"+entity));
        return ArticleDto.builder()
                .id(entity.getId())
                .habitId(entity.getHabit().getId())
                .userAccountDto(UserAccountDto.from(entity.getUserAccount()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }

    public Article toEntity(Habit habit, UserAccount userAccount) {
        return Article.builder()
                .id(id)
                .habit(habit)
                .userAccount(userAccount)
                .title(title)
                .content(content)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .modifiedAt(modifiedAt)
                .modifiedBy(modifiedBy)
                .build();
    }
}
