package io.github.dszopa.message_router.helper.no_type

import io.github.dszopa.message_router.annotation.MessageController
import io.github.dszopa.message_router.annotation.Route
import org.eclipse.jetty.websocket.api.Session

var nonTypedMessageHandlerCalled: Boolean = false
var nonTypedMessageHandlerMessage: String = ""

@MessageController
class AnnotatedClassNonCustom {

    @Route("nonTypedMessageHandler")
    fun nonTypedMessageHandler(user: Session, message: String) {
        nonTypedMessageHandlerCalled = true
        nonTypedMessageHandlerMessage = message
    }
}