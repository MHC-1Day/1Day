package com.mhc.haveit.controller;

import com.mhc.haveit.config.TestSecurityConfig;
import com.mhc.haveit.domain.type.FormStatus;
import com.mhc.haveit.domain.type.SearchType;
import com.mhc.haveit.dto.ArticleWithCommentDto;
import com.mhc.haveit.dto.HabitDto;
import com.mhc.haveit.dto.HabitWithArticlesDto;
import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.service.HabitService;
import com.mhc.haveit.service.PaginationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 습관")
@Import(TestSecurityConfig.class)
@WebMvcTest(HabitController.class)
class HabitControllerTest {

    private final MockMvc mvc;
    @MockBean private HabitService habitService;
    @MockBean private PaginationService paginationService;

    public HabitControllerTest(@Autowired MockMvc mvc) { this.mvc = mvc; }

    @DisplayName("[view][GET] 습관 리스트 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingHabitsView_thenReturnsHabitsView() throws Exception {
        // Given
        given(habitService.searchHabits(eq(null),eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4,5));

        // When & Then
        mvc.perform(get("/habits"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("habits/index"))
                .andExpect(model().attributeExists("habits"))
                .andExpect(model().attributeExists("searchTypes"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(habitService).should().searchHabits(eq(null),eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }

    @DisplayName("[view][GET] 습관 리스트 페이지 - 검색어와 함께 호출")
    @Test
    void givenSearchKeyword_whenRequestingHabitsView_thenReturnsHabitsView() throws Exception {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "study";
        given(habitService.searchHabits(eq(searchType),eq(searchKeyword), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(1,2,3,4,5));

        // When & Then
        mvc.perform(get("/habits")
                        .queryParam("searchType",searchType.name())
                        .queryParam("searchKeyword",searchKeyword)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("habits/index"))
                .andExpect(model().attributeExists("habits"))
                .andExpect(model().attributeExists("searchTypes"))
                .andExpect(model().attributeExists( "paginationBarNumbers"));
        then(habitService).should().searchHabits(eq(searchType),eq(searchKeyword), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }

    @DisplayName("[view][GET] 습관 상세 페이지 - 인증 되지않은 사용자는 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequestingHabitView_thenRedirectsToLoginPage() throws Exception {
        // Given
        Long habitId = 1L;

        // When & Then
        mvc.perform(get("/habits/"+habitId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        then(habitService).shouldHaveNoInteractions();
    }

    @WithMockUser   // 인증된 MockUser 를 사용
    @DisplayName("[view][GET] 습관 상세 페이지 - 정상 호출, 인증된 사용자")
    @Test
    void givenNothing_whenRequestingHabitView_thenReturnsHabitView() throws Exception {
        // Given
        Long habitId = 1L;
        HabitWithArticlesDto habitWithArticlesDto = createHabitWithArticleDto();
        ArticleWithCommentDto articleWithCommentDto = createArticleWithCommentDto();
        given(habitService.getHabitWithArticles(habitId)).willReturn(habitWithArticlesDto);

        // When & Then
        mvc.perform(get("/habits/"+habitId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("habits/detail"))
                .andExpect(model().attributeExists("habit"))
                .andExpect(model().attributeExists("articles"));
        then(habitService).should().getHabitWithArticles(habitId);
    }

    @WithMockUser       // 인증된 MockUser 를 사용
    @DisplayName("[view][GET] 새 습관 작성 페이지")
    @Test
    void givenNothing_whenRequesting_thenNewHabitPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/habits/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("habits/form"))
                .andExpect(model().attribute("formStatus", FormStatus.CREATE));

    }

    /*
        test method 시작 전에 사용
        TestSecurityConfig UserAccount 정보를 가져 옴
        userDetailsServiceBeanName  bean 이 하나일 때는 생략 가능 함
     */
    @WithUserDetails(value = "jshTest", setupBefore = TestExecutionEvent.TEST_EXECUTION )
    @DisplayName("[view][POST] 새 습관 생성")
    @Test
    void givenNewHabitInfo_whenRequesting_thenSavesNewHabit() throws Exception {
        // Given
        String habitEncoded = "name=new title&content=new content&hashtag=#new&endDate=2017-06-01";
        willDoNothing().given(habitService).saveHabit(any(HabitDto.class));

        // When & Then
        mvc.perform(
                post("/habits/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(habitEncoded)
                        .with(csrf())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/habits"))
                .andExpect(redirectedUrl("/habits"))
                        .andDo(print());
        then(habitService).should().saveHabit(any(HabitDto.class));
    }

    @DisplayName("[view][GET] 습관 수정 페이지 - 인증 되지않은 사용자는 로그인 페이지로 이동")
    @Test
    void givenNothing_whenRequestingHabitUpdateView_thenRedirectsToLoginPage() throws Exception {
        // Given
        Long habitId = 1L;

        // When & Then
        mvc.perform(get("/habits/"+habitId+"/form"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
        then(habitService).shouldHaveNoInteractions();
    }

    @DisplayName("[view][GET] 습관 수정 페이지 - 정상 호출, 인증된 사용자")
    @WithMockUser
    @Test
    void givenHabitIdNothing_whenRequesting_thenUpdateHabitPage() throws Exception {
        // Given
        Long habitId = 1L;
        HabitDto habitDto = createHabitDto();
        given(habitService.getHabit(habitId)).willReturn(habitDto);

        // When & Then
        mvc.perform(get("/habits/"+habitId+"/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("habits/form"))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE))
                .andExpect(model().attributeExists("habit"));
        then(habitService).should().getHabit(habitId);
    }

    @WithUserDetails(value = "jshTest", setupBefore = TestExecutionEvent.TEST_EXECUTION )
    @DisplayName("[view][POST] 습관 수정")
    @Test
    void givenUpdatedHabitInfo_whenRequesting_thenUpdatesHabit() throws Exception {
        // Given
        Long habitId = 1L;
        String habitEncoded = "name=new title&content=new content&hashtag=#new&endDate=2017-06-01";
        willDoNothing().given(habitService).updateHabit(eq(habitId),any(HabitDto.class));

        // When & Then
        mvc.perform(
                        post("/habits/"+habitId+"/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .content(habitEncoded)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/habits/"+habitId))
                .andExpect(redirectedUrl("/habits/"+habitId))
                .andDo(print());
        then(habitService).should().updateHabit(eq(habitId),any(HabitDto.class));
    }

    @WithUserDetails(value = "jshTest", setupBefore = TestExecutionEvent.TEST_EXECUTION )
    @DisplayName("[view][POST] 습관 삭제")
    @Test
    void givenHabitId_whenRequesting_thenDeletesHabit() throws Exception {
        // Given
        Long habitId = 1L;
        String userId = "jshTest";
        willDoNothing().given(habitService).deleteHabit(habitId,userId);

        // When & Then
        mvc.perform(
                        post("/habits/"+habitId+"/delete")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/habits"))
                .andExpect(redirectedUrl("/habits"))
                .andDo(print());
        then(habitService).should().deleteHabit(habitId,userId);
    }

    static HabitWithArticlesDto createHabitWithArticleDto(){
        return HabitWithArticlesDto.builder()
                .id(1L)
                .userAccountDto(createUserAccountDto())
                .articleWithCommentDtos(Set.of())
                .name("study")
                .content("java study")
                .build();
    }

    static HabitDto createHabitDto(){
        return HabitDto.builder()
                .id(1L)
                .userAccountDto(createUserAccountDto())
                .name("habitName")
                .content("habitContent")
                .hashtag("habitHashtag")
                .endDate(LocalDateTime.now())
                .build();
    }

    static UserAccountDto createUserAccountDto(){
        return UserAccountDto.builder()
                .userId("jsh")
                .nickname("Jeong")
                .email("jsh@mail.com")
                .build();
    }

    private ArticleWithCommentDto createArticleWithCommentDto() {
        return ArticleWithCommentDto.builder()
                .title("title")
                .content("content")
                .userAccountDto(createUserAccountDto())
                .commentDtos(Set.of())
                .build();
    }
}
