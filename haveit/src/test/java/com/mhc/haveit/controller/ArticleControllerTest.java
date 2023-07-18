package com.mhc.haveit.controller;

import com.mhc.haveit.config.TestSecurityConfig;
import com.mhc.haveit.domain.type.FormStatus;
import com.mhc.haveit.dto.ArticleDto;
import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import(TestSecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;
    @MockBean  private ArticleService articleService;

    public ArticleControllerTest(@Autowired MockMvc mvc) { this.mvc = mvc; }

    @WithMockUser
    @DisplayName("[view][GET] 게시글 작성 페이지")
    @Test
    void givenHabitId_whenRequesting_thenNewArticlePage() throws Exception {
        // Given
        long habitId = 1L;

        // When & Then
        // /habits/{habitId}/articles/form
        mvc.perform(get("/articles/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("formStatus", FormStatus.CREATE));
    }

    @WithUserDetails(value = "jshTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 생성")
    @Test
    void givenNewArticleInfo_whenRequesting_thenNewArticle() throws Exception {
        // Given
        long habitId = 1L;
        String articleEncoded = "habitId=1&title=new article&content=new content";
        willDoNothing().given(articleService).saveArticle(any(ArticleDto.class));

        // When & Then
        mvc.perform(
                post("/articles/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(articleEncoded)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/habits/"+habitId))
                .andExpect(redirectedUrl("/habits/"+habitId))
                .andDo(print());
        then(articleService).should().saveArticle(any(ArticleDto.class));

    }

    @WithMockUser
    @DisplayName("[view][GET] 게시글 수정 페이지")
    @Test
    void givenArticleId_whenRequesting_thenUpdatePage() throws Exception {
        // Given
        long habitId = 1L;
        long articleId = 1L;
        ArticleDto articleDto = createArticleDto("newTitle","newContent");
        given(articleService.getArticle(articleId)).willReturn(articleDto);

        // When & Then
        mvc.perform(get("/articles/"+articleId+"/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("formStatus",FormStatus.UPDATE))
                .andExpect(model().attributeExists("article"));
        then(articleService).should().getArticle(articleId);
    }

    @WithUserDetails(value = "jshTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 수정")
    @Test
    void givenUpdateArticleInfo_whenRequesting_thenUpdatesArticle() throws Exception {
        // Given
        long habitId = 1L;
        long articleId = 1L;
        String articleEncoded = "habitId=1&title=new article&content=new content";
        willDoNothing().given(articleService).updateArticle(eq(articleId),any(ArticleDto.class));

        // When & Then
        mvc.perform(
                post("/articles/"+articleId+"/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(articleEncoded)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/habits/"+habitId))
                .andExpect(redirectedUrl("/habits/"+habitId))
                .andDo(print());
        then(articleService).should().updateArticle(eq(articleId),any(ArticleDto.class));
    }

    @WithUserDetails(value = "jshTest", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 삭제")
    @Test
    void givenArticleId_whenRequesting_thenDeletesArticle() throws Exception {
        // Given
        long habitId = 1L;
        long articleId = 1L;
        String userId = "jshTest";
        String habitIdEncoded = "habitId=1";
        willDoNothing().given(articleService).deleteArticle(articleId,userId);

        // When & Then
        mvc.perform(
                post("/articles/"+articleId+"/delete")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(habitIdEncoded)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/habits/"+habitId))
                .andExpect(redirectedUrl("/habits/"+habitId))
                .andDo(print());
        then(articleService).should().deleteArticle(articleId,userId);

    }

    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.builder()
                .id(1L)
                .habitId(1L)
                .userAccountDto(createUserAccountDto())
                .title(title)
                .content(content)
                .build();
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.builder()
                .id(1L)
                .userId("jsh")
                .userPassword("pw")
                .nickname("Jeong")
                .build();

    }
}