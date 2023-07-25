package com.mhc.haveit.controller;

import com.mhc.haveit.dto.request.HabitRegistrationRequest;
import com.mhc.haveit.dto.security.HavitPrincipal;
import com.mhc.haveit.service.HabitRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/habitRegistration")
@Controller
public class HabitRegistrationController {

    private final HabitRegistrationService habitRegistrationService;

    @PostMapping("/new")
    public String postNewHabitRegistration(
            @AuthenticationPrincipal HavitPrincipal havitPrincipal,
            HabitRegistrationRequest habitRegistrationRequest
    ){
        habitRegistrationService.saveHabitRegistration(habitRegistrationRequest.toDto(havitPrincipal.toDto()));
        return "redirect:/habits/"+habitRegistrationRequest.getHabitId();
    }

    @PostMapping("/delete")
    public String postDeleteHabitRegistration(
            @AuthenticationPrincipal HavitPrincipal havitPrincipal,
            HabitRegistrationRequest habitRegistrationRequest
    ){
        habitRegistrationService.deleteHabitRegistration(habitRegistrationRequest.getHabitId(), havitPrincipal.getUsername());
        return "redirect:/habits/"+habitRegistrationRequest.getHabitId();
    }

}
