package com.mhc.haveit.controller;

import com.mhc.haveit.config.SecurityConfig;
import com.mhc.haveit.service.HabitService;
import com.mhc.haveit.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
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

    @Disabled("개발중")
    @DisplayName("[view][GET] 습관 상세 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingHabitView_thenReturnsHabitView() throws Exception {
        mvc.perform(get("/habits/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("habits/detail"))
                .andExpect(model().attributeExists("habit"));
    }

}