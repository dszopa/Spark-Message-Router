package improved.spark.sockets

import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject

// TODO: consider renaming
class RouteMapper {

    private var routes: HashMap<String, RouteHandler> = HashMap()

    fun addRoute(route: String, handler: Class<*>) {
        routes.put(route, (handler.newInstance() as RouteHandler))
    }

    fun mapToRoute(user: Session, message: String) {
        val jsonObject: JSONObject = JSONObject(message)

        if (jsonObject.has("route")) {
            val route: String = jsonObject.getString("route")
            val routeHandler: RouteHandler? = routes.get(route)

            // Make sure the route is in the map
            if (routeHandler != null) {

                // Create the message string minus the route
                val withoutRoute: JSONObject = JSONObject(message)
                withoutRoute.remove("route")

                // Handle the route
                routeHandler.handle(user, withoutRoute)
            }
        }
    }
}