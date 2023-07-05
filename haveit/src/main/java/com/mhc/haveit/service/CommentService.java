package com.mhc.haveit.service;

import com.mhc.haveit.domain.Article;
import com.mhc.haveit.domain.UserAccount;
import com.mhc.haveit.dto.CommentDto;
import com.mhc.haveit.repository.ArticleRepository;
import com.mhc.haveit.repository.CommentRepository;
import com.mhc.haveit.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final UserAccountRepository userAccountRepository;
    private final ArticleRepository articleRepository;

    public void saveComment(CommentDto dto) {
        Article article = articleRepository.getReferenceById(dto.getArticleId());
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getId());
        commentRepository.save(dto.toEntity(article,userAccount));
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
