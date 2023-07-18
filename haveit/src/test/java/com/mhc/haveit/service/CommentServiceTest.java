package com.mhc.haveit.service;

import com.mhc.haveit.domain.Article;
import com.mhc.haveit.domain.Comment;
import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.UserAccount;
import com.mhc.haveit.dto.ArticleDto;
import com.mhc.haveit.dto.CommentDto;
import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.repository.ArticleRepository;
import com.mhc.haveit.repository.CommentRepository;
import com.mhc.haveit.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks private CommentService sut;

    @Mock private CommentRepository commentRepository;
    @Mock private ArticleRepository articleRepository;
    @Mock private UserAccountRepository userAccountRepository;

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenCommentInfo_whenSaving_thenSavesComment() {
        // Given
        CommentDto commentDto = createCommentDto("content");
        given(commentRepository.save(any(Comment.class))).willReturn(null);
        given(articleRepository.getReferenceById(any(Long.class))).willReturn(createArticle());
        given(userAccountRepository.getReferenceById(any(Long.class))).willReturn(createuserAccount());

        // When
        sut.saveComment(commentDto);

        // Then
        then(commentRepository).should().save(any(Comment.class));
        then(articleRepository).should().getReferenceById(any(Long.class));
        then(userAccountRepository).should().getReferenceById(any(Long.class));

    }

    @DisplayName("댓글 ID 를 입력하면, 댓글을 삭제한다.")
    @Test
    void givenCommentId_whenSaving_thenSavesComment() {
        // Given
        Long commentId = 1L;
        String userAccountId = "jshTest";
        willDoNothing().given(commentRepository).deleteByIdAndUserAccount_UserId(commentId,userAccountId);

        // When
        sut.deleteComment(commentId,userAccountId);

        // Then
        then(commentRepository).should().deleteByIdAndUserAccount_UserId(commentId,userAccountId);
    }

    private CommentDto createCommentDto(String content) {
        return CommentDto.builder()
                .id(1L)
                .articleId(1L)
                .userAccountDto(createUserAccountDto())
                .content(content)
                .build();
    }

    private ArticleDto createArticleDto(String title, String content){
        return ArticleDto.builder()
                .id(1L)
                .habitId(1L)
                .title(title)
                .content(content)
                .userAccountDto(createUserAccountDto())
                .build();
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.builder()
                .id(1L)
                .nickname("Jeong")
                .email("jsh@mail.com")
                .memo("memo")
                .createdAt(LocalDateTime.now())
                .createdBy("jsh")
                .modifiedAt(LocalDateTime.now())
                .modifiedBy("jsh")
                .build();
    }

    private UserAccount createuserAccount(){
        return UserAccount.builder()
                .id(1L)
                .nickname("Jeong")
                .email("jsh@mail.com")
                .memo("memo")
                .build();
    }

    private Article createArticle() {
        return Article.builder()
                .id(1L)
                .habit(createHabit())
                .title("title")
                .content("content")
                .build();
    }

    private Habit createHabit(){
        return Habit.builder()
                .id(1L)
                .name("Spring")
                .userAccount(createuserAccount())
                .content("study")
                .hashtag("#java")
                .build();
    }
}