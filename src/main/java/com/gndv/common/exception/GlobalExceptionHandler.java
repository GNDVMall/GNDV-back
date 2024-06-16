package com.gndv.common.exception;

import com.gndv.common.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public CustomResponse<Object> handleAllExceptions(Exception ex) {
        log.error("Exception: ", ex);
        return CustomResponse.error("An error occurred: " + ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public CustomResponse<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("IllegalArgumentException: ", ex);
        return CustomResponse.failure("Invalid input: " + ex.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public CustomResponse<Object> handleCustomException(CustomException ex) {
        log.error("CustomException: ", ex);
        return CustomResponse.failure(ex.getMessage());
    }
}
