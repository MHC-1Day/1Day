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
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "habit_id"),
        @Index(columnList = "user_account_id")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class HabitRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @ManyToOne(optional = false) private Habit habit;
    @Setter @ManyToOne(optional = false) private UserAccount userAccount;

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

    private HabitRegistration(Habit habit, UserAccount userAccount) {
        this.habit = habit;
        this.userAccount = userAccount;
    }

    protected HabitRegistration () {}

    public static HabitRegistration of (Habit habit, UserAccount userAccount) {
        return new HabitRegistration(habit, userAccount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HabitRegistration habitRegistration)) return false;
        return id != null && id.equals(habitRegistration.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
