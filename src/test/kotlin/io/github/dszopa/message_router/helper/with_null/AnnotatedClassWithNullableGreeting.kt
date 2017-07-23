package io.github.dszopa.message_router.helper.with_null

import io.github.dszopa.message_router.annotation.MessageController
import io.github.dszopa.message_router.annotation.MessageObject
import io.github.dszopa.message_router.annotation.Route
import org.eclipse.jetty.websocket.api.Session

var nullableGreetingHandlerCalled: Boolean = false
var nullableGreetingHandlerGreeting: Greeting? = null

@MessageController
class AnnotatedClassWithCustomParam {

    @Route("nullableGreetingHandler")
    fun nullableGreetingHandler(user: Session, @MessageObject greeting: Greeting) {
        nullableGreetingHandlerCalled = true
        nullableGreetingHandlerGreeting = greeting
    }
}