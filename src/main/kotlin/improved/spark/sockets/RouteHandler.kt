package improved.spark.sockets

import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject

// TODO: consider renaming
interface  RouteHandler {
    fun handle(user: Session, message: JSONObject)
}