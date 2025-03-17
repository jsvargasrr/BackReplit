package com.nubo.masterparticipant.api.application.interceptors;

import com.nubo.masterparticipant.api.application.context.GlobalContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Order(2)
@RequiredArgsConstructor
public class GlobalInterceptor implements HandlerInterceptor {

    private final RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        GlobalContext.remove();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        rateLimitInterceptor.rateLimitInterceptor(request, handler);
        return true;
    }

}