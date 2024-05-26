package com.bespoke.Bespoke.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class PageNotFoundController {


    // THIS HANDLES NO RESOURCE FOUND ERRORS
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoHandlerFoundException() {
        return "PageNotFound";
    }
}

