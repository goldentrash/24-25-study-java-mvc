package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        registerPackages(basePackage);
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
        return handlerExecutions.get(handlerKey);
    }

    private void registerPackages(final Object[] packages) {
        Arrays.stream(packages).map(Reflections::new)
                .map(r -> r.getTypesAnnotatedWith(Controller.class))
                .flatMap(Collection::stream)
                .forEach(this::registerController);
    }

    private void registerController(final Class<?> controllerClass) {
        String baseValue = controllerClass.getAnnotation(Controller.class).value();

        Consumer<Method> registerMethod = genRegisterMethod(controllerClass, baseValue);
        Arrays.stream(controllerClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(RequestMapping.class))
                .forEach(registerMethod);
    }

    private Consumer<Method> genRegisterMethod(final Class<?> controllerClass, final String baseValue) {
        return method -> {
            HandlerExecution handlerExecution = new HandlerExecution(controllerClass, method);

            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            String url = baseValue + requestMapping.value();
            for (final RequestMethod m : requestMapping.method()) {
                HandlerKey handlerKey = new HandlerKey(url, m);
                handlerExecutions.put(handlerKey, handlerExecution);
                log.debug("Registered handler `{}` for \"{} {}\"", method.getName(), m, url);
            }
        };
    }
}
