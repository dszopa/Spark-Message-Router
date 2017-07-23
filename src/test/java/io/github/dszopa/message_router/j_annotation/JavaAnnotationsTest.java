package io.github.dszopa.message_router.j_annotation;

import io.github.dszopa.message_router.annotation.MessageController;
import io.github.dszopa.message_router.annotation.MessageObject;
import io.github.dszopa.message_router.annotation.Route;
import io.github.dszopa.message_router.helper.j_with_type.JavaAnnotatedClassWithType;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JavaAnnotationsTest {

    @Test
    public void verifyAnnotations() throws Exception {
        Class<?> clazz = Class.forName(JavaAnnotatedClassWithType.class.getName()) ;
        assertNotNull(clazz);

        assertTrue(clazz.isAnnotationPresent(MessageController.class));

        for (Method method : clazz.getDeclaredMethods()) {
            assertTrue(method.isAnnotationPresent(Route.class));
            assertTrue(method.getAnnotation(Route.class).value().equals("javaAnnotatedClassWithTypeHandler"));
            for (Parameter parameter : method.getParameters()) {
                if (parameter.getName().equals("greeting")) {
                    assertTrue(parameter.isAnnotationPresent(MessageObject.class));
                }
            }
        }
    }
}
