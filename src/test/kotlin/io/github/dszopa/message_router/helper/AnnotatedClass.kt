package io.github.dszopa.message_router.helper

import io.github.dszopa.message_router.annotation.MessageController
import io.github.dszopa.message_router.annotation.MessageObject
import io.github.dszopa.message_router.annotation.Route
import org.eclipse.jetty.websocket.api.Session

var annotatedClassValue = 0

@MessageController
class AnnotatedClass {
    @Route("test")
    fun annotatedFunction(user: Session, @MessageObject greeting:Greeting) {
        annotatedClassValue = 1
    }
}