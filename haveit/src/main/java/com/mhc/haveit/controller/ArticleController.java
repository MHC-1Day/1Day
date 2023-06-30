package com.mhc.haveit.controller;

import com.mhc.haveit.domain.type.FormStatus;
import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.dto.request.ArticleRequest;
import com.mhc.haveit.dto.response.ArticleResponse;
import com.mhc.haveit.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/habits/{habitId}/articles")
@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/form")
    public String newArticleForm(@PathVariable String habitId, ModelMap modelMap){
        modelMap.addAttribute("formStatus", FormStatus.CREATE);
        modelMap.addAttribute("habitId",habitId);
        return "articles/form";
    }

    @PostMapping("/form")
    public String postNewArticle(
            @PathVariable Long habitId,
            ArticleRequest articleRequest
    ){
        // TODO : 인증 정보를 넣어야합니다.
        UserAccountDto dummyAccount = UserAccountDto.builder()
                .id(1L)
                .userId("jsh")
                .userPassword("pw")
                .email("jsh@mail.com")
                .build();

        articleService.saveArticle(articleRequest.toDto(dummyAccount));
        return "redirect:/habits/"+habitId;
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(
            @PathVariable Long articleId,
            @PathVariable Long habitId,
            ModelMap modelMap
    ){
        ArticleResponse articleResponse = ArticleResponse.from(articleService.getArticle(articleId));
        modelMap.addAttribute("habitId",habitId);
        modelMap.addAttribute("article", articleResponse);
        modelMap.addAttribute("formStatus",FormStatus.UPDATE);
        return "articles/form";
    }

    @PostMapping("/{articleId}/form")
    public String postUpdateArticle(
            @PathVariable Long articleId,
            @PathVariable Long habitId,
            ArticleRequest articleRequest
    ){
        // TODO : 인증 정보를 넣어야합니다.
        UserAccountDto dummyAccount = UserAccountDto.builder()
                .id(1L)
                .userId("jsh")
                .userPassword("pw")
                .email("jsh@mail.com")
                .build();

        articleService.updateArticle(articleId,articleRequest.toDto(dummyAccount));
        return "redirect:/habits/"+habitId;
    }

    @PostMapping("/{articleId}/delete")
    public String deleteArticle(
            @PathVariable Long articleId,
            @PathVariable String habitId
    ){
        articleService.deleteArticle(articleId);
        return "redirect:/habits/"+habitId;
    }

}
