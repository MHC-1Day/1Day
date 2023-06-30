package com.mhc.haveit.dto.request;

import com.mhc.haveit.dto.ArticleDto;
import com.mhc.haveit.dto.HabitDto;
import com.mhc.haveit.dto.UserAccountDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequest {
    private Long habitId;
    private String title;
    private String content;


    public ArticleDto toDto(UserAccountDto userAccountDto){
        return ArticleDto.builder()
                .userAccountDto(userAccountDto)
                .habitId(habitId)
                .title(title)
                .content(content)
                .build();
    }
}
