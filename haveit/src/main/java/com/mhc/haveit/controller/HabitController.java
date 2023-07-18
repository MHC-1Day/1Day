package com.mhc.haveit.controller;

import com.mhc.haveit.domain.type.FormStatus;
import com.mhc.haveit.domain.type.SearchType;
import com.mhc.haveit.dto.request.HabitRequest;
import com.mhc.haveit.dto.response.HabitResponse;
import com.mhc.haveit.dto.response.HabitWithArticlesResponse;
import com.mhc.haveit.dto.security.HavitPrincipal;
import com.mhc.haveit.service.HabitService;
import com.mhc.haveit.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/habits")
@Controller
public class HabitController {

    private final HabitService habitService;
    private final PaginationService paginationService;

    private final int DEFAULT_SIZE = 12;

    @GetMapping
    public String habits(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchKeyword,
            @PageableDefault(size = DEFAULT_SIZE, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map
    ) {
        Page<HabitResponse> habits = habitService.searchHabits(searchType, searchKeyword, pageable).map(HabitResponse::from);
        List<Integer> barNumber = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), habits.getTotalPages());

        map.addAttribute("habits", habits);
        map.addAttribute("paginationBarNumbers",barNumber);
        map.addAttribute("searchTypes",SearchType.values());

        return "habits/index";
    }

    @GetMapping("/{habitId}")
    public String habit( @PathVariable Long habitId, ModelMap map) {
        HabitWithArticlesResponse habit = HabitWithArticlesResponse.from(habitService.getHabitWithArticles(habitId));
        map.addAttribute("habit",habit);
        map.addAttribute("articles",habit.getArticleWithCommentResponses());

        return "habits/detail";
    }

    @GetMapping("/form")
    public String newHabitForm(ModelMap modelMap){
        modelMap.addAttribute("formStatus", FormStatus.CREATE);
        return "habits/form";
    }

    @PostMapping("/form")
    public String postNewHabit(
            @AuthenticationPrincipal HavitPrincipal havitPrincipal,
            HabitRequest habitRequest
    ){
        habitService.saveHabit(habitRequest.toDto(havitPrincipal.toDto()));
        return "redirect:/habits";
    }

    @GetMapping("/{habitId}/form")
    public String updateHabitFrom(@PathVariable Long habitId, ModelMap modelMap){
        HabitResponse habitResponse = HabitResponse.from(habitService.getHabit(habitId));
        modelMap.addAttribute("habit",habitResponse);
        modelMap.addAttribute("formStatus",FormStatus.UPDATE);
        return "habits/form";
    }

    @PostMapping("/{habitId}/form")
    public String postNewHabit(
            @PathVariable Long habitId,
            @AuthenticationPrincipal HavitPrincipal havitPrincipal,
            HabitRequest habitRequest
    ){
        habitService.updateHabit(habitId,habitRequest.toDto(havitPrincipal.toDto()));
        return "redirect:/habits/"+habitId;
    }

    @PostMapping("/{habitId}/delete")
    public String postDeleteHabit(
            @PathVariable Long habitId,
            @AuthenticationPrincipal HavitPrincipal havitPrincipal
            ){
        habitService.deleteHabit(habitId,havitPrincipal.getUsername());
        return "redirect:/habits";
    }

}
