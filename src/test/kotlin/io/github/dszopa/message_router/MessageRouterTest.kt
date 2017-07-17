package io.github.dszopa.message_router

import org.eclipse.jetty.websocket.api.Session
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.test.assertTrue

class MessageRouterTest {
    @Test fun TestMessageRouter() {
        assertTrue(annotatedClassValue == 0)
        val messageRouter: MessageRouter = MessageRouter("io.github.dszopa.message_router")
        val mockSession: Session = mock(Session::class.java)
        messageRouter.handle(mockSession, "{ route: test }")
        assertTrue(annotatedClassValue == 1)
    }
}