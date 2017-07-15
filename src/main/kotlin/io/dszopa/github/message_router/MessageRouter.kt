package io.dszopa.github.message_router

import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject

class MessageRouter {

    private var routes: HashMap<String, RouteHandler> = HashMap()

    fun addRoute(route: String, handler: Class<*>) {
        routes.put(route, (handler.newInstance() as RouteHandler))
    }

    fun mapToRoute(user: Session, message: String) {
        val messageJson: JSONObject = JSONObject(message)

        if (messageJson.has("route")) {
            val route: String = messageJson.getString("route")
            val routeHandler: RouteHandler? = routes.get(route)

            if (routeHandler != null) {
                messageJson.remove("route")
                routeHandler.handle(user, messageJson)
            }
        }
    }
}