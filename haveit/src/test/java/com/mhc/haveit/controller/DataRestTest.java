package com.mhc.haveit.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Spring Data REST 통합테스트는 불필요하므로 제외시킴")
@DisplayName("Data REST - API 테스트")
@Transactional                          // 데이터베이스를 직접 건드릴 수 있으므로 안전하게 롤백을 하기위해
@AutoConfigureMockMvc
@SpringBootTest
public class DataRestTest {

    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 습관 리스트 조회")
    @Test
    void givenNothing_whenRequesting_thenReturnsHabitsResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/habits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));   // json 과는 달라 직접 설정
    }

    @DisplayName("[api] 습관 단건 조회")
    @Test
    void givenNothing_whenRequesting_thenReturnsHabitResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/habits/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 습관의 게시글 리스트 조회")
    @Test
    void givenNothing_whenRequesting_thenReturnsHabitArticlesResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/habits/1/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 게시글의 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequesting_thenReturnsArticleCommentsResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }
}
