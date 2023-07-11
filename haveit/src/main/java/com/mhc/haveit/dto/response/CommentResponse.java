package com.mhc.haveit.dto.response;

import com.mhc.haveit.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;

    public static CommentResponse from(CommentDto dto){
        Optional.ofNullable(dto).orElseThrow(()-> new IllegalArgumentException("잘못된 dto 입니다. - dto:"+dto));

        String nickname = dto.getUserAccountDto().getNickname();
        if(nickname.isBlank()){
            nickname = dto.getUserAccountDto().getUserId();
        }
        return CommentResponse.builder()
                .id(dto.getId())
                .nickname(nickname)
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .build();
    }

}
