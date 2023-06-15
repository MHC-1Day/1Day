package com.mhc.haveit.dto;

import com.mhc.haveit.domain.Article;
import com.mhc.haveit.domain.UserAccount;
import lombok.*;

import java.io.Serializable;

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

    public ArticleDto from(Article entity){
        return ArticleDto.builder()
                .id(entity.getId())
                .habitId(entity.getHabit().getId())
                .userAccountDto(UserAccountDto.from(entity.getUserAccount()))
                .title(entity.getTitle())
                .content(entity.getContent())
                .build();
    }

}
