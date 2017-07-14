import j2html.TagCreator.*
import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject
import spark.Spark.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Predicate

object Chat {

    // this map is shared between sessions and threads, so it needs to be thread-safe (http://stackoverflow.com/a/2688817)
    internal var userUsernameMap: ConcurrentHashMap<Session, String> = ConcurrentHashMap()
    internal var nextUserNumber = 1 //Assign to username for next connecting user

    @JvmStatic fun main(args: Array<String>) {
        staticFiles.location("/public") //index.html is served at localhost:4567 (default port)
        staticFiles.expireTime(600)
        webSocket("/chat", ChatWebSocketHandler::class.java)
        init()
    }

    //Sends a message from one user to all users, along with a list of current usernames
    fun broadcastMessage(sender: String, message: String) {
        userUsernameMap.keys.stream().filter(Predicate<Session> { it.isOpen() }).forEach { session ->
            try {
                session.remote.sendString(JSONObject()
                        .put("userMessage", createHtmlMessageFromSender(sender, message))
                        .put("userlist", userUsernameMap.values).toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //Builds a HTML element with a sender-name, a message, and a timestamp,
    private fun createHtmlMessageFromSender(sender: String, message: String): String {
        return article(
                b(sender + " says:"),
                span(attrs(".timestamp"), SimpleDateFormat("HH:mm:ss").format(Date())),
                p(message)
        ).render()
    }

}