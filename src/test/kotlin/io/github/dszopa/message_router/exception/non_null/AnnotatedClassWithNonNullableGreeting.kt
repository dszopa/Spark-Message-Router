package io.github.dszopa.message_router.exception.non_null

import io.github.dszopa.message_router.annotation.MessageController
import io.github.dszopa.message_router.annotation.MessageObject
import io.github.dszopa.message_router.annotation.Route
import org.eclipse.jetty.websocket.api.Session

@MessageController
class AnnotatedClassWithNonNullableGreeting {

    @Route("customTest")
    fun customAnnotatedFunction(user: Session, @MessageObject greeting: NonNullableGreeting) {}
}