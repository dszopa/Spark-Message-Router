
import improved.spark.sockets.RouteMapper
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import routes.SimpleRoute

@WebSocket
class ChatWebSocketHandler {

    private val routeMapper: RouteMapper = RouteMapper()

    private var sender: String? = null
    private var msg: String? = null

    init {
        // Map routes to implementation classes here
        routeMapper.addRoute("simpleRoute", SimpleRoute::class.java)
    }


    @OnWebSocketConnect
    @Throws(Exception::class)
    fun onConnect(user: Session) {
        Chat.nextUserNumber = Chat.nextUserNumber + 1
        val username = "User" + Chat.nextUserNumber
        Chat.userUsernameMap.put(user, username)
        Chat.broadcastMessage("Server", username + "joined the chat")
    }

    @OnWebSocketClose
    fun onClose(user: Session, statusCode: Int, reason: String) {
        val username = Chat.userUsernameMap[user]
        Chat.userUsernameMap.remove(user)
        Chat.broadcastMessage("Server",username + " left the chat")
    }

    @OnWebSocketMessage
    fun onMessage(user: Session, message: String) {
        routeMapper.mapToRoute(user, message)
    }
}
