package io.github.dszopa.message_router.exception.param_mismatch

import io.github.dszopa.message_router.annotation.MessageController
import io.github.dszopa.message_router.annotation.Route
import org.eclipse.jetty.websocket.api.Session

@MessageController
class AnnotatedClassWithParamMismatch {

    @Route("customTest")
    fun customAnnotatedFunction(user: Session) {}
}