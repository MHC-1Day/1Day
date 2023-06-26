package com.mhc.haveit.dto;

import com.mhc.haveit.domain.Article;
import com.mhc.haveit.domain.UserAccount;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

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

}
