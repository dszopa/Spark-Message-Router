import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket

@WebSocket
class ChatWebSocketHandler {

    private var sender: String? = null
    private var msg: String? = null

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
        // TODO: i think it makes sense to send messages as json strings, this way pathing can easily be implemented, as well as headers
        // TODO: implement pathing
        val user = Chat.userUsernameMap[user]
        if (user != null) {
            Chat.broadcastMessage(user, message)
        }
    }
}