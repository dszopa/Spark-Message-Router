package io.github.dszopa.message_router.annotation;

import io.github.dszopa.message_router.helper.JavaAnnotatedClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JavaAnnotationsTest {

    @Test
    public void verifyAnnotations() throws Exception {
        Class<?> clazz = Class.forName(JavaAnnotatedClass.class.getName()) ;
        assertNotNull(clazz);

        assertTrue(clazz.isAnnotationPresent(MessageController.class));

        for (Method method : clazz.getDeclaredMethods()) {
            assertTrue(method.isAnnotationPresent(Route.class));
            assertTrue(method.getAnnotation(Route.class).value().equals("jTest"));
            for (Parameter parameter : method.getParameters()) {
                if (parameter.getName().equals("greeting")) {
                    assertTrue(parameter.isAnnotationPresent(MessageObject.class));
                }
            }
        }
    }
}
