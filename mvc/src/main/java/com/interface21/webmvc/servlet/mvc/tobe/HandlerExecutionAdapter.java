package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request, final HttpServletResponse response,
            final Object handler) throws Exception {
        if (!supports(handler)) {
            throw new IllegalArgumentException("This handler is not supported: " + handler);
        }

        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
