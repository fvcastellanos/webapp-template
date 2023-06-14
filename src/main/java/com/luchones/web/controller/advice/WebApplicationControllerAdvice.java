package com.luchones.web.controller.advice;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class WebApplicationControllerAdvice extends ResponseEntityExceptionHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WebApplicationControllerAdvice.class);

    @ExceptionHandler
    public ModelAndView handleFallBackException(final Exception exception, final WebRequest request) {

        final var model = new HashMap<String, Object>();
        model.put("message", "Unable to process request");
        model.put("error", exception.getMessage());
        model.put("exception", exception);

        LOGGER.info("Handle fallback exception");

        return new ModelAndView("error/index", model);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAuthenticationException(final AccessDeniedException exception, final WebRequest request) {

        LOGGER.info("Handle access denied exception");
        
        final var principal = request.getUserPrincipal();

        final var model = new HashMap<String, Object>();
        model.put("message", "Authentication Exception");
        model.put("error", String.format("Access denied to user: %s", principal.getName()));

        return new ModelAndView("error/index", model);
    }
}
