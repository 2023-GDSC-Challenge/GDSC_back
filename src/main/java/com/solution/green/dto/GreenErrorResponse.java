package com.solution.green.dto;

import com.solution.green.code.GreenErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GreenErrorResponse {
    private GreenErrorCode errorCode;
    private String errorMessage;
}