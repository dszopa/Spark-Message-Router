package io.dszopa.github.message_router

import io.dszopa.github.message_router.annotation.MessageController
import io.dszopa.github.message_router.annotation.Route
import org.eclipse.jetty.websocket.api.Session

var annotatedClassValue = 0

@MessageController
class AnnotatedClass {
    @Route("test")
    fun annotatedFunction(user: Session, message: String) {
        annotatedClassValue = 1
    }
}