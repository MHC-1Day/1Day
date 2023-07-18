package com.mhc.haveit.controller;

import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.dto.request.CommentRequest;
import com.mhc.haveit.dto.security.HavitPrincipal;
import com.mhc.haveit.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/comments")
@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/new")
    public String postNewComment(
            @AuthenticationPrincipal HavitPrincipal havitPrincipal,
            CommentRequest commentRequest,
            Long habitId
    ){
        commentService.saveComment(commentRequest.toDto(havitPrincipal.toDto()));
        return "redirect:/habits/"+habitId;
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal HavitPrincipal havitPrincipal,
            Long habitId
    ){
        commentService.deleteComment(commentId,havitPrincipal.getUsername());
        return "redirect:/habits/"+habitId;
    }
}
