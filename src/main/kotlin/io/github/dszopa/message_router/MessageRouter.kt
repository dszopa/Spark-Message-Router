package io.github.dszopa.message_router

import com.google.gson.Gson
import io.github.dszopa.message_router.annotation.MessageObject
import io.github.dszopa.message_router.annotation.Route
import io.github.dszopa.message_router.exception.NonNullableTypeException
import io.github.dszopa.message_router.exception.ParameterMissmatchException
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult
import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmErasure

/**
 * A router for socket messages
 *
 * This class will use reflection to find all functions annotated with
 * "@Route" and will map the functions to their route values.
 *
 * @param packagePath the package to be scanned for routes.
 * @constructor Creates a new message router with routes found
 *      through reflection.
 */
class MessageRouter(packagePath: String) {

    private val routeToMethodCalls: HashMap<String, Triple<Any, KFunction<*>, Type?>> = HashMap()
    private val gson: Gson = Gson()

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

                    if ((function.parameters.size != 3) || (function.parameters[1].type.jvmErasure != Session::class) ||
                             ((function.parameters[2].type.jvmErasure !=  String::class) && (!function.parameters[2].annotations.any { it.annotationClass == MessageObject::class}))) {
                        throw ParameterMissmatchException("Incorrect parameters. First paramter should be of type Session. Second parameter should be of type String or include the @MessageObject annotation.")
                    }

                    var param3: Type? = null
                    if (function.parameters[2].annotations.any { it.annotationClass == MessageObject::class}) {
                        param3 = function.parameters[2].type.javaType

                        val messageObjectParameters = function.parameters[2].type.jvmErasure.primaryConstructor!!.parameters
                        for (parameter: KParameter in messageObjectParameters) {
                            if (!parameter.type.isMarkedNullable) {
                                throw NonNullableTypeException("Objects annotated with @MessageObject must allow nullable values for all publicly available variables.")
                            }
                        }
                    }

                    val route: Route? = function.findAnnotation<Route>() // We already confirmed that it has a route annotation
                    if (route != null) {
                        val value: String = route.value
                        routeToMethodCalls.put(value, Triple(instance, function, param3))
                    }
                }
            }
        }
    }

    /**
     *  Routes incoming socket message to a function and calls that
     *  function with the given parameters.
     * @param user A session object that indicates the user who sent the socket message.
     * @param message The socket message, this must be in JSON format
     *      and include a "route" value in order for routing to work.
     */
    fun handle(user: Session, message: String) {
        val messageJson: JSONObject = JSONObject(message)

        if (messageJson.has("route") && routeToMethodCalls.containsKey(messageJson.getString("route"))) {
            val route: String = messageJson.getString("route")
            val methodTriple: Triple<Any, KFunction<*>, Type?>? = routeToMethodCalls[route]

            if (methodTriple?.third != null) {
                methodTriple.second.call(methodTriple.first, user, gson.fromJson(message, methodTriple.third))
            } else {
                methodTriple?.second?.call(methodTriple.first, user, message)
            }
        }
    }
}