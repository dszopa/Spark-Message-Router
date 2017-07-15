package routes
import Chat
import improved.spark.sockets.RouteHandler
import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject

class SimpleRoute : RouteHandler {

    override fun handle(user: Session, message: JSONObject) {
        val user = Chat.userUsernameMap[user]
        if (user != null && message.has("message")) {
            Chat.broadcastMessage(user, message.getString("message"))
        }
    }
}
