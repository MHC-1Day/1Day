package com.mhc.haveit.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Habit habit;
    @Setter @ManyToOne(optional = false) private UserAccount userAccount;

    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false , length = 1000) private String content;

    @ToString.Exclude
    @OrderBy("createdAt DESC ")
    @OneToMany(mappedBy = "article" , cascade = CascadeType.ALL)        // TODO : 게시글 삭제 시, 댓글도 삭제 됨 이후에 soft delete 변경 필요
    private final Set<Comment> comments = new LinkedHashSet<>();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    private String createdBy;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @LastModifiedBy
    @Column(nullable = false, updatable = false, length = 100)
    private String modifiedBy;

    protected Article() {}

    private Article (Habit habit, UserAccount userAccount, String title, String content) {
        this.habit = habit;
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    public static Article of (Habit habit, UserAccount userAccount, String title, String content) {
       return new Article(habit, userAccount, title, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
