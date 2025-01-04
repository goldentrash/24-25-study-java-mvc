package com.techcourse;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HandlerAdapterRegistry {
    private final Set<HandlerAdapter> handlerAdapters;

    public HandlerAdapterRegistry() {
        this.handlerAdapters = new HashSet<>();
    }

    public void registerHandlerAdapter(
            final Class<? extends HandlerAdapter> handlerClass, final Object... args)
            throws ReflectiveOperationException {
        HandlerAdapter handlerAdapter;
        try {
            handlerAdapter = ReflectionUtils
                    .accessibleConstructor(handlerClass, getArgsClass(args))
                    .newInstance(args);
        } catch (NoSuchMethodException e) {
            handlerAdapter = ReflectionUtils
                    .accessibleConstructor(handlerClass, Object[].class)
                    .newInstance((Object) args);
        }
        handlerAdapters.add(handlerAdapter);
    }

    public HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElse(null);
    }

    private Class<?>[] getArgsClass(Object... args) {
        return Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class[]::new);
    }
}
