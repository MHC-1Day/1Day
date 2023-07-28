package com.mhc.haveit.service;

import com.mhc.haveit.domain.Article;
import com.mhc.haveit.domain.UserAccount;
import com.mhc.haveit.dto.CommentDto;
import com.mhc.haveit.repository.ArticleRepository;
import com.mhc.haveit.repository.CommentRepository;
import com.mhc.haveit.repository.HabitRegistrationRepository;
import com.mhc.haveit.repository.UserAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final UserAccountRepository userAccountRepository;
    private final ArticleRepository articleRepository;
    private final HabitRegistrationRepository habitRegistrationRepository;

    public void saveComment(CommentDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.getArticleId());
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getId());

            boolean isExist = habitRegistrationRepository.existsHabitRegistrationByHabit_IdAndUserAccount_UserId(article.getHabit().getId(), userAccount.getUserId());
            if(!isExist){
                log.warn("댓글 저장 실패, 습관에 등록된 유저가 아닙니다. ");
                return;
            }

            commentRepository.save(dto.toEntity(article, userAccount));
        }catch (EntityNotFoundException e){
            log.warn("댓글 저장 실패, 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}",e.getLocalizedMessage());
        }
    }

    public void deleteComment(Long commentId, String userAccountId) {
        commentRepository.deleteByIdAndUserAccount_UserId(commentId,userAccountId);
    }
}
