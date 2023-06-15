package com.mhc.haveit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/habits")
@Controller
public class HabitController {
    @GetMapping
    public String habits(ModelMap map){
        map.addAttribute("habits", List.of());  // TODO : service 구현 시 바꿔야 함
        return "habits/index";
    }
}
