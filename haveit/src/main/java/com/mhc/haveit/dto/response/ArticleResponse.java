package com.mhc.haveit.dto.response;

import com.mhc.haveit.dto.ArticleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

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
        Optional.ofNullable(dto).orElseThrow(()-> new IllegalArgumentException("잘못된 dto 입니다. - dto:"+dto));

        String nickname = dto.getUserAccountDto().getNickname();
        if(nickname.isBlank()){
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
