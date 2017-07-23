package io.github.dszopa.message_router.helper.non_null

import io.github.dszopa.message_router.annotation.MessageController
import io.github.dszopa.message_router.annotation.MessageObject
import io.github.dszopa.message_router.annotation.Route
import org.eclipse.jetty.websocket.api.Session

var nonNullableGreetingHandlerCalled: Boolean = false
var nonNullableGreetingHandlerGreeting: NonNullableGreeting? = null

@MessageController
class AnnotatedClassWithNonNullableGreeting {

    @Route("nonNullableGreetingHandler")
    fun nonNullableGreetingHandler(user: Session, @MessageObject greeting: NonNullableGreeting) {
        nonNullableGreetingHandlerCalled = true
        nonNullableGreetingHandlerGreeting = greeting
    }
}