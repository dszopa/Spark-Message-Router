package io.dszopa.github.message_router

import io.dszopa.github.message_router.annotation.Route
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult
import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.jvmErasure

class MessageRouter(packagePath: String) {

    private val routeToMethodCalls: HashMap<String, Pair<Any, KFunction<*>>> = HashMap()

    init {
        val scanResult: ScanResult = FastClasspathScanner(packagePath)
                .enableMethodAnnotationIndexing()
                .scan()

        val classesContainingRouteMethods: List<String> = scanResult.getNamesOfClassesWithMethodAnnotation(Route::class.java)

        for (className: String in classesContainingRouteMethods) {
            val clazz: KClass<*> = Class.forName(className).kotlin
            val instance: Any = clazz.createInstance()

            for (function: KFunction<*> in clazz.declaredFunctions) {
                if (function.annotations.any { it.annotationClass == Route::class}) {

                    if (!function.parameters.any { it.type.jvmErasure == String::class } ||
                            !function.parameters.any { it.type.jvmErasure == Session::class }) {
                        throw Error("Missing String and / or Session parameter types on function: ${function.name}")
                    }

                    val route: Route? = function.findAnnotation<Route>() // We already confirmed that it has a route annotation
                    if (route != null) {
                        val value: String = route.value
                        routeToMethodCalls.put(value, Pair(instance, function))
                    }
                }
            }
        }
    }

    fun handle(user: Session, message: String) {
        val messageJson: JSONObject = JSONObject(message)

        if (messageJson.has("route") && routeToMethodCalls.containsKey(messageJson.getString("route"))) {
            val route: String = messageJson.getString("route")
            val methodPair: Pair<Any, KFunction<*>>? = routeToMethodCalls[route]
            methodPair?.second?.call(methodPair.first, user, message)
        }
    }
}