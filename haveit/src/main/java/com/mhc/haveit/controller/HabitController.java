package com.mhc.haveit.controller;

import com.mhc.haveit.domain.type.SearchType;
import com.mhc.haveit.dto.HabitDto;
import com.mhc.haveit.service.HabitService;
import com.mhc.haveit.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Page<HabitDto> habits = habitService.searchHabits(searchType, searchKeyword, pageable);
        List<Integer> barNumber = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), habits.getTotalPages());

        map.addAttribute("habits", habits);
        map.addAttribute("paginationBarNumbers",barNumber);
        map.addAttribute("searchTypes",SearchType.values());

        return "habits/index";
    }
}
