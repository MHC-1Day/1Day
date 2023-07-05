package com.mhc.haveit.dto.request;

import com.mhc.haveit.dto.CommentDto;
import com.mhc.haveit.dto.UserAccountDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private Long articleId;
    private String content;

    public CommentDto toDto(UserAccountDto userAccountDto){
        return CommentDto.builder()
                .articleId(articleId)
                .userAccountDto(userAccountDto)
                .content(content)
                .build();
    }

}
