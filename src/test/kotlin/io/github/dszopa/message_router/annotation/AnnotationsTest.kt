package io.github.dszopa.message_router.annotation

import io.github.dszopa.message_router.helper.non_null.AnnotatedClassWithNonNullableGreeting
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AnnotationsTest {

    @Test fun verifyAnnotations() {
        val clazz: KClass<*> = Class.forName(AnnotatedClassWithNonNullableGreeting::class.qualifiedName).kotlin
        assertNotNull(clazz)

        val kClassAnnotations = clazz.annotations.map { it.annotationClass }
        val kFunctionAnnotations = ArrayList<KClass<out Annotation>>()
        clazz.declaredFunctions.map { it.annotations.map { kFunctionAnnotations.add(it.annotationClass) } }

        assertTrue(kClassAnnotations.contains(MessageController::class))
        assertTrue(kFunctionAnnotations.contains(Route::class))

        val route: Route? = clazz.declaredFunctions.find { it.name == "nonNullableGreetingHandler" }?.findAnnotation<Route>()

        assertNotNull(route)
        assertTrue(route?.value == "nonNullableGreetingHandler")

        for (function in clazz.declaredFunctions) {
            // True because AnnotationClass only has one function with route
            if (function.annotations.any { it.annotationClass == Route::class }) {
                assertTrue(function.parameters[2].annotations.any { it.annotationClass == MessageObject::class })
            }
        }
    }
}