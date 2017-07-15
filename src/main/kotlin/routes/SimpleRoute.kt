package routes

import io.dszopa.github.message_router.annotation.MessageController
import io.dszopa.github.message_router.annotation.Route
import org.eclipse.jetty.websocket.api.Session

@MessageController
class SimpleRoute {

    @Route("simpleRoute")
    fun helloWorld(user: Session, message: String) {
        println("Hello World!");
    }
}
