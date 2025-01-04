package com.techcourse;

import com.interface21.core.util.ReflectionUtils;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HandlerMappingRegistry {
    private final Set<HandlerMapping> handlerMappings;

    public HandlerMappingRegistry() {
        this.handlerMappings = new HashSet<>();
    }

    public void registerHandlerMapping(
            final Class<? extends HandlerMapping> handlerMappingClass, Object... args)
            throws ReflectiveOperationException {
        HandlerMapping handlerMapping;
        try {
            handlerMapping = ReflectionUtils
                    .accessibleConstructor(handlerMappingClass, getArgsClass(args))
                    .newInstance(args);
        } catch (NoSuchMethodException e) {
            handlerMapping = ReflectionUtils
                    .accessibleConstructor(handlerMappingClass, Object[].class)
                    .newInstance((Object) args);
        }
        handlerMapping.initialize();

        handlerMappings.add(handlerMapping);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.supports(request))
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .findFirst()
                .orElse(null);
    }

    private Class<?>[] getArgsClass(Object... args) {
        return Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class[]::new);
    }
}
