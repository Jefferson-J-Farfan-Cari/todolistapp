package com.todolist.app.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class NotFoundException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;

    public NotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
