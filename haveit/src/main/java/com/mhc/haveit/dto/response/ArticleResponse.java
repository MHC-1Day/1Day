package com.mhc.haveit.dto.response;

import com.mhc.haveit.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponse{
    private Long id;
    private String title;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public static ArticleResponse from(ArticleDto dto) {
        String nickname = dto.getUserAccountDto().getNickname();

        if(nickname == null || nickname.isBlank()){
            nickname = dto.getUserAccountDto().getUserId();
        }

        return ArticleResponse.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .nickname(nickname)
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
