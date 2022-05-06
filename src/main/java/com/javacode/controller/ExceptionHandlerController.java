package com.javacode.controller;

import com.javacode.exceptions.UnauthorizedException;
import io.swagger.annotations.Api;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Api(tags = "Exception")
public class ExceptionHandlerController {

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<?> handlerException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
    }
}
