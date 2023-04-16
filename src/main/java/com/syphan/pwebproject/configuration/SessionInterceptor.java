package com.syphan.pwebproject.configuration;

import com.syphan.pwebproject.model.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        if (request.getRequestURI().equals("/login")) {
            if (session != null && session.getAttribute("user") != null) {
                response.sendRedirect("/");
                return false;
            }
        }
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return false;
        }

        UserDto user = (UserDto) session.getAttribute("user");

        if (user.getUserType() != 3 && request.getRequestURI().matches("^/users.*$")) {
            response.sendRedirect("/");
            return false;
        }

        if(user.getUserType() == 1 && (request.getRequestURI().matches("^/questions.*$") || request.getRequestURI().equals("/exams/new"))) {
            response.sendRedirect("/");
            return false;
        }

        return true;
    }
}