package com.interface21.web.handler.mapping;

import com.interface21.core.util.ReflectionUtils;
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
        // TODO: too complex now
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

    // TODO: exception handling
    public Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(mapping -> mapping.supports(request))
                .map(mapping -> mapping.getHandler(request))
                .findFirst()
                .orElse(null);
    }

    private Class<?>[] getArgsClass(Object... args) {
        return Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class[]::new);
    }
}
