package com.mhc.haveit.controller;

import com.mhc.haveit.config.SecurityConfig;
import com.mhc.haveit.dto.CommentDto;
import com.mhc.haveit.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 댓글")
@Import(SecurityConfig.class)
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    private final MockMvc mvc;
    @MockBean private CommentService commentService;

    public CommentControllerTest(@Autowired MockMvc mvc) { this.mvc = mvc; }

    // 생성
    // 삭제

    @DisplayName("[view][POST] 댓글 생성")
    @Test
    void name() throws Exception {
        // Given
        long habitId = 1L;
        long articleId = 35L;
        String commentEncoded = "articleId=1&content=new content&habitId=1";
        willDoNothing().given(commentService).saveComment(any(CommentDto.class));

        // When & Then
        mvc.perform(
                post("/comments/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(commentEncoded)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/habits/"+habitId))
                .andExpect(redirectedUrl("/habits/"+habitId))
                .andDo(print());
        then(commentService).should().saveComment(any(CommentDto.class));
    }
}