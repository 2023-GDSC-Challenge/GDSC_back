package com.solution.green.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public enum GreenCode {
    QUEST_ING(false),
    QUEST_DONE(true),
    ;
    @Nullable
    private final Boolean bool;
}
