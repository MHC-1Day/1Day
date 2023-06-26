package com.mhc.haveit.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FormStatus {
    CREATE("저장",false),
    UPDATE("수정",true);

    private final String description;
    private final boolean isUpdate;
}
