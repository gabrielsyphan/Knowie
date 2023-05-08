package com.syphan.pwebproject.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ModelAndView handleError(HttpServletRequest request, Exception e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", e.getMessage());
        mav.addObject("url", request.getRequestURL());
        mav.addObject("errors", e.getStackTrace());
        return mav;
    }
}