package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object handler;
    private final Method method;

    public HandlerExecution(final Class<?> controllerClass, final Method method) {
        try {
            this.handler = ReflectionUtils.accessibleConstructor(controllerClass).newInstance();
            this.method = method;
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("There is no default constructor or it is not accessible", e);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(handler, request, response);
    }
}
