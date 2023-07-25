package com.mhc.haveit.service;

import com.mhc.haveit.domain.Article;
import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.UserAccount;
import com.mhc.haveit.dto.ArticleDto;
import com.mhc.haveit.dto.UserAccountDto;
import com.mhc.haveit.repository.ArticleRepository;
import com.mhc.haveit.repository.HabitRegistrationRepository;
import com.mhc.haveit.repository.HabitRepository;
import com.mhc.haveit.repository.UserAccountRepository;
import jakarta.persistence.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    @InjectMocks private ArticleService sut;
    @Mock private ArticleRepository articleRepository;
    @Mock private HabitRepository habitRepository;
    @Mock private UserAccountRepository userAccountRepository;
    @Mock private HabitRegistrationRepository habitRegistrationRepository;
    
    @DisplayName("습관 ID 로 조회하면, 해당하는 게시글 리스트를 반환한다.")
    @Test
    void givenHabitId_whenSearchingArticle_thenReturnsArticles() {
        // Given
        Long habitId = 1L;
        Pageable pageable = Pageable.ofSize(5);
        given(articleRepository.findByHabit_Id(habitId,pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchArticles(habitId,pageable);

        // Then
        assertThat(articles).isEqualTo(Page.empty());
        then(articleRepository).should().findByHabit_Id(habitId,pageable);
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 저장한다.")
    @Test
    void givenArticleInfo_whenSaving_thenSavesArticle(){
        // Given
        ArticleDto articleDto = createArticleDto("title","content",createUserAccountDto());
        given(articleRepository.save(any(Article.class))).willReturn(createArticle());
        given(habitRepository.getReferenceById(articleDto.getHabitId())).willReturn(createHabit());
        given(userAccountRepository.getReferenceById(articleDto.getUserAccountDto().getId())).willReturn(createuserAccount());
        given(habitRegistrationRepository.existsHabitRegistrationByHabit_IdAndUserAccount_UserId(anyLong(),anyString())).willReturn(true);


        // When
        sut.saveArticle(articleDto);

        // Then
        then(articleRepository).should().save(any(Article.class));
        then(habitRepository).should().getReferenceById(articleDto.getHabitId());
        then(userAccountRepository).should().getReferenceById(articleDto.getUserAccountDto().getId());
        then(habitRegistrationRepository).should().existsHabitRegistrationByHabit_IdAndUserAccount_UserId(anyLong(),anyString());
    }

    @DisplayName("없는 습관의 게시글 정보를 입력하면, 예외를 던지고 아무것도 하지않는다.")
    @Test
    void givenNoneExistHabit_whenSaving_thenThrowsExceptionAndDoseNothing(){
        // Given
        ArticleDto articleDto = createArticleDto("wrongTitle","wrongContent",createUserAccountDto());
        given(habitRepository.getReferenceById(articleDto.getHabitId())).willThrow(EntityNotFoundException.class);

        // When
        sut.saveArticle(articleDto);

        // Then
        then(habitRepository).should().getReferenceById(articleDto.getHabitId());
        then(articleRepository).shouldHaveNoInteractions();
        then(userAccountRepository).shouldHaveNoInteractions();
    }

    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedArticleInfo_whenUpdating_thenUpdatesArticle(){
        // Given
        Long articleId = 1L;
        Article article = createArticle();
        ArticleDto updateArticleDto = createArticleDto("updateTitle","updateContent",createUserAccountDto());
        given(articleRepository.getReferenceById(updateArticleDto.getId())).willReturn(article);

        // When
        sut.updateArticle(articleId,updateArticleDto);

        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title",updateArticleDto.getTitle())
                .hasFieldOrPropertyWithValue("content",updateArticleDto.getContent());
        then(articleRepository).should().getReferenceById(updateArticleDto.getId());
    }

    @DisplayName("다른 유저가 게시글의 수정 정보를 입력하면, 아무것도 하지않는다.")
    @Test
    void givenAnotherUserModifiedArticleInfo_whenUpdating_thenNothing(){
        // Given
        Long articleId = 1L;
        Article article = createArticle();
        ArticleDto updateArticleDto = createArticleDto("updateTitle","updateContent",createAnotherUserAccountDto());
        given(articleRepository.getReferenceById(articleId)).willReturn(article);

        // When
        sut.updateArticle(articleId,updateArticleDto);

        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title",article.getTitle())
                .hasFieldOrPropertyWithValue("content",article.getContent());
        then(articleRepository).should().getReferenceById(updateArticleDto.getId());
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 예외를 던지고 아무것도 하지않는다.")
    @Test
    void givenNoneExistArticleInfo_whenUpdating_thenThrowsExceptionsAndDoseNothing(){
        // Given
        Long articleId = 1L;
        ArticleDto updateArticleDto = createArticleDto("updateTitle","updateContent",createUserAccountDto());
        given(articleRepository.getReferenceById(updateArticleDto.getId())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticle(articleId,updateArticleDto);

        // Then
        then(articleRepository).should().getReferenceById(updateArticleDto.getId());
    }

    @DisplayName("게시글 ID 를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeleting_thenDeletesArticle(){
        // Given
        Long articleId = 1L;
        String userId = "jsh";
        willDoNothing().given(articleRepository).deleteByIdAndUserAccount_UserId(articleId,userId);

        // When
        sut.deleteArticle(articleId,userId);

        // Then
        then(articleRepository).should().deleteByIdAndUserAccount_UserId(articleId,userId);
    }

    @DisplayName("해당 습관의 ID 로 게시글 수를 조회하면, 습관의 게시글 수를 반환한다.")
    @Test
    void givenHabitId_whenCountArticles_thenReturnsCount(){
        // Given
        Long habitId = 1L;
        Long expected = 10L;
        given(articleRepository.countByHabit_Id(habitId)).willReturn(expected);

        // When
        Long actual = sut.getHabitArticleCount(habitId);

        // Then
        assertThat(actual).isEqualTo(expected);
        then(articleRepository).should().countByHabit_Id(habitId);
    }

    @DisplayName("게시글 수를 조회하면, 모든 게시글 수를 반환한다.")
    @Test
    void givenNothing_whenCountArticles_thenReturnsArticleCount(){
        // Given
        Long habitId = 1L;
        Long expected = 10L;
        given(articleRepository.count()).willReturn(expected);

        // When
        Long actual = sut.getArticleCount();

        // Then
        assertThat(actual).isEqualTo(expected);
        then(articleRepository).should().count();
    }

    private ArticleDto createArticleDto(String title, String content, UserAccountDto userAccountDto){
        return ArticleDto.builder()
                .id(1L)
                .habitId(1L)
                .title(title)
                .content(content)
                .userAccountDto(userAccountDto)
                .build();
    }
    private Article createArticle() {
        return Article.builder()
                .id(1L)
                .habit(createHabit())
                .userAccount(createuserAccount())
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

    private UserAccount createuserAccount(){
        return UserAccount.builder()
                .id(1L)
                .userId("jsh")
                .nickname("Jeong")
                .email("jsh@mail.com")
                .memo("memo")
                .build();
    }

    private UserAccountDto createAnotherUserAccountDto(){
        return UserAccountDto.builder()
                .id(2L)
                .nickname("anotherUser")
                .email("anotherUser@mail.com")
                .memo("anotherUser")
                .createdAt(LocalDateTime.now())
                .createdBy("anotherUser")
                .modifiedAt(LocalDateTime.now())
                .modifiedBy("anotherUser")
                .build();
    }

    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.builder()
                .id(1L)
                .userId("jsh")
                .nickname("Jeong")
                .email("jsh@mail.com")
                .memo("memo")
                .createdAt(LocalDateTime.now())
                .createdBy("jsh")
                .modifiedAt(LocalDateTime.now())
                .modifiedBy("jsh")
                .build();
    }
}
