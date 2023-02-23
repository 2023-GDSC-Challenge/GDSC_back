package com.solution.green.exception;

import com.solution.green.dto.GreenErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import static com.solution.green.code.GreenErrorCode.INTERNAL_SERVER_ERROR;
import static com.solution.green.code.GreenErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class GreenExceptionHandler {
    @ExceptionHandler(GreenException.class) // DMakerException 관련 에러
    public GreenErrorResponse handleException (
            GreenException e,
            HttpServletRequest request){
        log.error("errorCode: {}, url: {}, message: {}",
                e.getGreenErrorCode(), request.getRequestURI(), e.getDetailMessage());
        return GreenErrorResponse.builder()
                .errorCode(e.getGreenErrorCode())
                .errorMessage(e.getDetailMessage())
                .build();
    }

    @ExceptionHandler(value = { // 특수한 경우
            HttpRequestMethodNotSupportedException.class,
            //ex. PostMapping 인데 다른 매핑을 적용할 때
            MethodArgumentNotValidException.class
            // ex. java bean validation 에서 문제 발생 -> 컨트롤러 내부에 진입도 못할 때
    })
    public GreenErrorResponse handleBadRequest (
            Exception e, // 서비스만의 특수한 상황 아님
            HttpServletRequest request){
        log.error("url: {}, message: {}",
                request.getRequestURI(), e.getMessage());

        return GreenErrorResponse.builder()
                .errorCode(INVALID_REQUEST)
                .errorMessage(INVALID_REQUEST.getMessage())
                .build();
    }

    @ExceptionHandler(Exception.class) // 이도저도아닌 모르는 케이스들
    public GreenErrorResponse handleException (
            Exception e,
            HttpServletRequest request){
        log.error("url: {}, message: {}",
                request.getRequestURI(), e.getMessage());

        return GreenErrorResponse.builder()
                .errorCode(INTERNAL_SERVER_ERROR)
                .errorMessage(INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }
}
