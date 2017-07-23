package io.github.dszopa.message_router.exception.duplicate_route

import io.github.dszopa.message_router.annotation.MessageController
import io.github.dszopa.message_router.annotation.Route
import org.eclipse.jetty.websocket.api.Session

@MessageController
class AnnotatedClassWithDuplicateRoute {

    @Route("duplicateRoute")
    fun duplicateRouteHandler1(user: Session, message: String) {}

    @Route("duplicateRoute")
    fun duplicateRouteHandler2(user: Session, message: String) {}
}