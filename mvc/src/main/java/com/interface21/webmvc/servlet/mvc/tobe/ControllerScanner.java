package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;

public class ControllerScanner {
    public Set<Class<?>> scan(final Object[] packages) {
        return Arrays.stream(packages).map(Reflections::new)
                .map(r -> r.getTypesAnnotatedWith(Controller.class))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
