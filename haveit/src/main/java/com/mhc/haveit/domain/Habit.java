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
@Table(indexes = {
        @Index(columnList = "name"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false) private String name;    // 습관이름   TODO : ERD 수정하기

    @Setter @ManyToOne(optional = false) private UserAccount userAccount;
    @Setter @Column(nullable = false, length = 1000) private String content;    // 내용
    @Setter private String hashtag;                                             // 해시태그
    @Setter @Column(nullable = false) private LocalDateTime endDate;            // 습관 종료일   TODO: ERD 수정하기

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "habit")
    private final Set<Article> articles = new LinkedHashSet<>();

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

    protected Habit() {}
    private Habit(String name, UserAccount userAccount, String content, String hashtag, LocalDateTime endDate) {
        this.name = name;
        this.userAccount = userAccount;
        this.content = content;
        this.hashtag = hashtag;
        this.endDate = endDate;
    }

    public static Habit of(String name, UserAccount userAccount, String content, String hashtag, LocalDateTime endDate) {
        return new Habit(name,userAccount,content,hashtag,endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Habit habit)) return false;
        return id != null && id.equals(habit.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
