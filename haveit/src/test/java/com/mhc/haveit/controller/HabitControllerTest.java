package com.mhc.haveit.controller;

import com.mhc.haveit.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest(HabitController.class)
@Import(SecurityConfig.class)
class HabitControllerTest {

    private final MockMvc mvc;

    public HabitControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 습관 리스트 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingHabitsView_thenReturnsHabitsView() throws Exception {
        mvc.perform(get("/habits"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("habits/index"))
                .andExpect(model().attributeExists("habits"));
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