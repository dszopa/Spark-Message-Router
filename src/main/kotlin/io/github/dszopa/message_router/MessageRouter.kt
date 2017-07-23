package io.github.dszopa.message_router

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import io.github.dszopa.message_router.annotation.MessageObject
import io.github.dszopa.message_router.annotation.Route
import io.github.dszopa.message_router.exception.DuplicateRouteException
import io.github.dszopa.message_router.exception.ParameterMismatchException
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult
import me.sargunvohra.lib.gsonkotlin.GsonKotlinAdapterFactory
import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
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

    private val logger: Logger = LoggerFactory.getLogger(MessageRouter::class.java)
    private val routeToMethodCalls: HashMap<String, Triple<Any, KFunction<*>, Type?>> = HashMap()
    private val kson: Gson = GsonBuilder()
            .registerTypeAdapterFactory(GsonKotlinAdapterFactory())
            .create()

    init {
        val scanResult: ScanResult = FastClasspathScanner(packagePath)
                .enableMethodAnnotationIndexing()
                .scan()

        val classesContainingRouteMethods: List<String> = scanResult.getNamesOfClassesWithMethodAnnotation(Route::class.java)

        for (className: String in classesContainingRouteMethods) {
            val clazz: KClass<*> = Class.forName(className).kotlin

            for (function: KFunction<*> in clazz.declaredFunctions) {
                if (function.annotations.any { it.annotationClass == Route::class}) {

                    if ((function.parameters.size != 3) || (function.parameters[1].type.jvmErasure != Session::class) ||
                             ((function.parameters[2].type.jvmErasure !=  String::class) && (!function.parameters[2].annotations.any { it.annotationClass == MessageObject::class}))) {
                        throw ParameterMismatchException(
                                "Incorrect parameters on method named: ${function.name}. " +
                                        "First parameter should be of type Session. Second parameter should be of type String or include the @MessageObject annotation."
                        )
                    }

                    var param3: Type? = null
                    if (function.parameters[2].annotations.any { it.annotationClass == MessageObject::class}) {
                        param3 = function.parameters[2].type.javaType
                    }

                    val route: Route = function.findAnnotation<Route>()!! // We already confirmed that it has a route annotation
                    val value: String = route.value
                    if (routeToMethodCalls.containsKey(value)) {
                        throw DuplicateRouteException("Cannot create duplicate route: '$value'")
                    }
                    val instance: Any = clazz.createInstance()
                    routeToMethodCalls.put(value, Triple(instance, function, param3))
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
                try {
                    if (!messageJson.has("data")) {
                        logger.warn("Invalid request to Route: '$route', mapped to method: '${methodTriple.second.name}'. " +
                                "Message did not contain a 'data' field.")
                        return
                    }
                    val messageObject: Any = kson.fromJson(messageJson.get("data").toString(), methodTriple.third)
                    methodTriple.second.call(methodTriple.first, user, messageObject)
                } catch (exception: JsonParseException) {
                    logger.warn("Invalid request to Route: '$route', mapped to method: '${methodTriple.second.name}'. " +
                            "MessageObject: '${methodTriple.third!!.typeName}' has non-null values that were populated as null.")
                }
            } else {
                methodTriple?.second?.call(methodTriple.first, user, message)
            }
        } else {
            logger.warn("Invalid request, message did not contain a 'route' field");
        }
    }
}