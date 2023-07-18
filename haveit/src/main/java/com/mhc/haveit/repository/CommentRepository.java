package com.mhc.haveit.repository;

import com.mhc.haveit.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByIdAndUserAccount_UserId(Long commentId, String UserId);
}
