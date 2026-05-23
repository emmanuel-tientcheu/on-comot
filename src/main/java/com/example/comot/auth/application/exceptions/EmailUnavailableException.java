package com.example.comot.auth.application.exceptions;

import com.example.comot.core.application.exceptions.BadRequestException;

public class EmailUnavailableException extends BadRequestException {
    public EmailUnavailableException() {
        super("this email is already use");
    }
}
