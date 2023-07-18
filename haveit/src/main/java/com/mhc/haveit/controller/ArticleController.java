package com.mhc.haveit.controller;

import com.mhc.haveit.domain.type.FormStatus;
import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.dto.request.ArticleRequest;
import com.mhc.haveit.dto.response.ArticleResponse;
import com.mhc.haveit.dto.security.HavitPrincipal;
import com.mhc.haveit.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/articles")
@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/form")
    public String newArticleForm(Long habitId, ModelMap modelMap){
        modelMap.addAttribute("formStatus", FormStatus.CREATE);
        modelMap.addAttribute("habitId",habitId);
        return "articles/form";
    }

    @PostMapping("/form")
    public String postNewArticle(
            @AuthenticationPrincipal HavitPrincipal havitPrincipal,
            ArticleRequest articleRequest,
            Long habitId
    ){
        articleService.saveArticle(articleRequest.toDto(havitPrincipal.toDto()));
        return "redirect:/habits/"+habitId;
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(
            @PathVariable Long articleId,
            Long habitId,
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
            @AuthenticationPrincipal HavitPrincipal havitPrincipal,
            Long habitId,
            ArticleRequest articleRequest
    ){
        articleService.updateArticle(articleId,articleRequest.toDto(havitPrincipal.toDto()));
        return "redirect:/habits/"+habitId;
    }

    @PostMapping("/{articleId}/delete")
    public String deleteArticle(
            @PathVariable Long articleId,
            @AuthenticationPrincipal HavitPrincipal havitPrincipal,
            String habitId
    ){
        articleService.deleteArticle(articleId,havitPrincipal.getUsername());
        return "redirect:/habits/"+habitId;
    }

}
