package com.example.comot.permission.infrastructure.spring.configuration;

import com.example.comot.permission.application.exceptions.PermissionAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class PermissionControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(PermissionAlreadyExistsException.class)
    public ResponseEntity<?> handlePermissionAlreadyExist(PermissionAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
