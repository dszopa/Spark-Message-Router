package io.github.dszopa.message_router.router

import io.github.dszopa.message_router.MessageRouter
import io.github.dszopa.message_router.exception.NonNullableTypeException
import io.github.dszopa.message_router.exception.ParameterMissmatchException
import io.github.dszopa.message_router.helper.annotatedClassCustomValue
import io.github.dszopa.message_router.helper.annotatedClassValue
import org.eclipse.jetty.websocket.api.Session
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.test.assertTrue

class MessageRouterTest {

    private val packagePath: String = "io.github.dszopa.message_router.helper"
    private val packagePrefixError: String = "io.github.dszopa.message_router.exception"
    private var messageRouter: MessageRouter? = null
    private var mockSession: Session? = null

    @Before
    fun setup() {
        messageRouter = MessageRouter(packagePath)
        mockSession = mock(Session::class.java)
    }

    @Test fun TestMessageRouter() {
        assertTrue(annotatedClassValue == 0)
        messageRouter!!.handle(mockSession!!, "{ route: test }")
        assertTrue(annotatedClassValue == 1)
    }

    @Test fun TestCustomMessageRouter() {
        assertTrue(annotatedClassCustomValue == 0)
        messageRouter!!.handle(mockSession!!, "{ route: 'customTest', name: 'nameo', exclamation: false }")
        assertTrue(annotatedClassCustomValue == 1)
    }

    @Test(expected = NonNullableTypeException::class) fun TestCustomMessageRouterNonNullableTypedMessage() {
        messageRouter = MessageRouter(packagePrefixError + ".non_null")
    }

    @Test(expected = ParameterMissmatchException::class) fun TestCustomMessageRouterInvalidParameters() {
        messageRouter = MessageRouter(packagePrefixError + ".param_mismatch")
    }

    // TODO: add tests for handle
}
