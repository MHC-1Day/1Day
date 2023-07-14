package com.mhc.haveit.service;

import com.mhc.haveit.domain.Article;
import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.UserAccount;
import com.mhc.haveit.dto.ArticleDto;
import com.mhc.haveit.dto.ArticleWithCommentDto;
import com.mhc.haveit.repository.ArticleRepository;
import com.mhc.haveit.repository.HabitRepository;
import com.mhc.haveit.repository.UserAccountRepository;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final HabitRepository habitRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(Long habitId, Pageable pageable) {
        return articleRepository.findByHabit_Id(habitId,pageable)
                .map(ArticleDto::from);
    }

    public void saveArticle(ArticleDto dto) {
        try {
            Habit habit = habitRepository.getReferenceById(dto.getHabitId());
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getId());
            articleRepository.save(dto.toEntity(habit,userAccount));
        }
        catch (EntityNotFoundException e){
            log.warn("게시글 저장 실패, 게시글 작성에 필요한 정보를 찾을 수 없습니다 - {}",e.getLocalizedMessage());
        }
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try{
            Article article = articleRepository.getReferenceById(articleId);
            if(dto.getTitle() != null){ article.setTitle(dto.getTitle()); }
            if(dto.getContent() != null){ article.setContent(dto.getContent()); }
        }catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto:{}",dto);
        }
    }

    public void deleteArticle(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    public Long getHabitArticleCount(Long habitId) {
        return articleRepository.countByHabit_Id(habitId);
    }

    public Long getArticleCount() {
        return articleRepository.count();
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId){
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 게시글 입니다. - articleId:"+articleId));
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentDto getArticleWithComment(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentDto::from)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 게시글 입니다. - articleId:"+articleId));
    }
}
