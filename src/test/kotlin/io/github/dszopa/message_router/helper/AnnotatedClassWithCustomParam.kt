package io.github.dszopa.message_router.helper

import io.github.dszopa.message_router.annotation.MessageController
import io.github.dszopa.message_router.annotation.MessageObject
import io.github.dszopa.message_router.annotation.Route
import org.eclipse.jetty.websocket.api.Session

var annotatedClassCustomValue: Int = 0;
var customClassGreeting: Greeting? = null

@MessageController
class AnnotatedClassWithCustomParam {

    @Route("customTest")
    fun customAnnotatedFunction(user: Session, @MessageObject greeting: Greeting) {
        customClassGreeting = greeting
        annotatedClassCustomValue = if (greeting.name == "nameo" && greeting.exclamation == false) 1 else  0
    }
}