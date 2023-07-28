package com.mhc.haveit.dto;

import com.mhc.haveit.domain.Habit;
import com.mhc.haveit.domain.HabitRegistration;
import com.mhc.haveit.domain.UserAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitRegistrationDto {
    private Long id;
    private Long habitId;
    private String userAccountId;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public HabitRegistration toEntity(Habit habit, UserAccount userAccount) {
        Optional.ofNullable(habit).orElseThrow(()-> new IllegalArgumentException("잘못된 entity 입니다. - "+Habit.class.getName()+":"+ habit));
        Optional.ofNullable(userAccount).orElseThrow(()-> new IllegalArgumentException("잘못된 entity 입니다. - "+UserAccount.class.getName()+":"+ userAccount));

        return HabitRegistration.builder()
                .id(id)
                .habit(habit)
                .userAccount(userAccount)
                .createdAt(createdAt)
                .createdBy(createdBy)
                .modifiedAt(modifiedAt)
                .modifiedBy(modifiedBy)
                .build();
    }

    public static HabitRegistrationDto from(HabitRegistration entity){
        Optional.ofNullable(entity).orElseThrow(()-> new IllegalArgumentException("잘못된 entity 입니다. - "+HabitRegistration.class.getName()+":"+entity));

        return HabitRegistrationDto.builder()
                .id(entity.getId())
                .habitId(entity.getHabit().getId())
                .userAccountId(entity.getUserAccount().getUserId())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}
