package com.mhc.haveit.controller;

import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.dto.request.CommentRequest;
import com.mhc.haveit.service.CommentService;
import lombok.RequiredArgsConstructor;
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
            CommentRequest commentRequest,
            Long habitId
    ){
        // TODO : 인증 정보를 넣어야합니다.
        UserAccountDto dummyAccount = UserAccountDto.builder()
                .id(1L)
                .userId("jsh")
                .userPassword("pw")
                .email("jsh@mail.com")
                .build();

        commentService.saveComment(commentRequest.toDto(dummyAccount));
        return "redirect:/habits/"+habitId;
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long commentId,
            Long habitId
    ){
        commentService.deleteComment(commentId);
        return "redirect:/habits/"+habitId;
    }
}
