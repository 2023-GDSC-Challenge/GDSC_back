package com.solution.green.exception;

import com.solution.green.code.GreenErrorCode;
import lombok.Getter;

@Getter
public class GreenException extends RuntimeException{
    private GreenErrorCode greenErrorCode;
    private String detailMessage;

    public GreenException(GreenErrorCode greenErrorCode){
        // for 일반적인 에러상황
        super(greenErrorCode.getMessage());
        this.greenErrorCode = greenErrorCode;
        this.detailMessage = greenErrorCode.getMessage();
    }

    public GreenException(GreenErrorCode greenErrorCode,
                           String detailMessage){
        // for 커스텀한 에러메세지를 출력해야할 때 사용
        super(greenErrorCode.getMessage());
        this.greenErrorCode = greenErrorCode;
        this.detailMessage = detailMessage;
    }
}
