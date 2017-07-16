package io.dszopa.github.message_router.annotation

import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@MessageController
class AnnotatedClass {
    @Route("test")
    fun annotatedFunction() {}
}

class MessageRouterTest {
    @Test fun annotationTypes() {
        val clazz: KClass<*> = Class.forName(AnnotatedClass::class.qualifiedName).kotlin
        assertNotNull(clazz)

        val kClassAnnotations = clazz.annotations.map { it.annotationClass }
        val kFunctionAnnotations = ArrayList<KClass<out Annotation>>()
        clazz.declaredFunctions.map { it.annotations.map { kFunctionAnnotations.add(it.annotationClass) } }

        assertTrue(kClassAnnotations.contains(MessageController::class))
        assertTrue(kFunctionAnnotations.contains(Route::class))

        val route: Route? = clazz.declaredFunctions.find { it.name == "annotatedFunction" }?.findAnnotation<Route>()

        assertNotNull(route)
        assertTrue(route?.value == "test")
    }
}