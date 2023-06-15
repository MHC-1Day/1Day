package com.mhc.haveit.dto;

import com.mhc.haveit.domain.UserAccount;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto implements Serializable {

    private Long id;
    private String userId;
    private String userPassword;
    private String nickname;
    private String email;
    private String memo;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

    public static UserAccountDto from(UserAccount entity){
        return UserAccountDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .userPassword(entity.getUserPassword())
                .nickname(entity.getNickname())
                .email(entity.getEmail())
                .memo(entity.getMemo())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedAt(entity.getModifiedAt())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }

    public UserAccount toEntity() {
        return UserAccount.builder()
                .id(id)
                .userId(userId)
                .userPassword(userPassword)
                .nickname(nickname)
                .email(email)
                .memo(memo)
                .build();
    }
}
