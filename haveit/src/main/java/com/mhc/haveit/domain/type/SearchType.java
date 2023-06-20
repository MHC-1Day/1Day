package com.mhc.haveit.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchType {
    TITLE("제목"),
    ID("아이디"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그");

    private final String description;
}
