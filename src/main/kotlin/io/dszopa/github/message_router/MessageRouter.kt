package io.dszopa.github.message_router

import io.dszopa.github.message_router.annotation.Route
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult
import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject
import java.lang.annotation.AnnotationTypeMismatchException
import java.lang.reflect.Method
import java.util.*

class MessageRouter(packagePath: String) {

    private val routeToMethodCalls: HashMap<String, Pair<Any, Method>> = HashMap()

    init {
        val scanResult: ScanResult = FastClasspathScanner(packagePath)
                .enableMethodAnnotationIndexing()
                .scan()

        val classesContainingRouteMethods: List<String> = scanResult.getNamesOfClassesWithMethodAnnotation(Route::class.java)

        for (className: String in classesContainingRouteMethods) {
            val clazz: Class<*> = Class.forName(className)
            val instance: Any = clazz.newInstance()

            for (method: Method in clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Route::class.java)) {
                    // Throw exception if Annotation doesn't have correct parameters
                    val parameterTypes: List<Class<*>> = method.parameterTypes.asList()
                    if (!parameterTypes.contains(String::class.java) || !parameterTypes.contains(Session::class.java)) {
                        throw  AnnotationTypeMismatchException(method, "Missing String and / or Session parameter types")
                    }
                    val route: Route = method.getAnnotation(Route::class.java)
                    routeToMethodCalls.put(route.value, Pair(instance, method))
                }
            }
        }
    }

    fun handle(user: Session, message: String) {
        val messageJson: JSONObject = JSONObject(message)
        if (messageJson.has("route") && routeToMethodCalls.containsKey(messageJson.getString("route"))) {
            val route: String = messageJson.getString("route")
            val methodPair: Pair<Any, Method>? = routeToMethodCalls[route]
            methodPair?.second?.invoke(methodPair.first, user, message)
        }
    }
}