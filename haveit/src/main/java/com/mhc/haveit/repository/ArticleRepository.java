package com.mhc.haveit.repository;

import com.mhc.haveit.domain.Article;
import com.mhc.haveit.dto.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByHabit_Id(Long habitId, Pageable pageable);
    Long countByHabit_Id(Long habitId);
    void deleteByIdAndUserAccount_UserId(Long articleId, String userId);
}
