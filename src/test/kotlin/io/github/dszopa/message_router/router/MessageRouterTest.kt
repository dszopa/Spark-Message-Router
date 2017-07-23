package io.github.dszopa.message_router.router

import io.github.dszopa.message_router.MessageRouter
import io.github.dszopa.message_router.exception.DuplicateRouteException
import io.github.dszopa.message_router.exception.ParameterMismatchException
import io.github.dszopa.message_router.helper.no_type.nonTypedMessageHandlerCalled
import io.github.dszopa.message_router.helper.no_type.nonTypedMessageHandlerMessage
import io.github.dszopa.message_router.helper.non_null.NonNullableGreeting
import io.github.dszopa.message_router.helper.non_null.nonNullableGreetingHandlerCalled
import io.github.dszopa.message_router.helper.non_null.nonNullableGreetingHandlerGreeting
import io.github.dszopa.message_router.helper.with_null.Greeting
import io.github.dszopa.message_router.helper.with_null.nullableGreetingHandlerCalled
import io.github.dszopa.message_router.helper.with_null.nullableGreetingHandlerGreeting
import org.eclipse.jetty.websocket.api.Session
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MessageRouterTest {

    private val packagePath: String = "io.github.dszopa.message_router.helper"
    private val packagePathError: String = "io.github.dszopa.message_router.exception"
    private var mockSession: Session? = null

    @Before
    fun setup() {
        mockSession = mock(Session::class.java)
        nonTypedMessageHandlerCalled = false
        nonTypedMessageHandlerMessage = ""
        nonNullableGreetingHandlerCalled = false
        nonNullableGreetingHandlerGreeting = null
        nullableGreetingHandlerCalled = false
        nullableGreetingHandlerGreeting = null
    }

    @Test(expected = ParameterMismatchException::class) fun messageRouterInvalidFunctionParams() {
        MessageRouter(packagePathError + ".param_mismatch")
    }

    @Test(expected = DuplicateRouteException::class) fun messageRouterDuplicateRoute() {
        MessageRouter(packagePathError + ".duplicate_route")
    }

    @Test fun handleNonCustom() {
        assertFalse(nonTypedMessageHandlerCalled)
        assertEquals(nonTypedMessageHandlerMessage, "")
        val messageRouter = MessageRouter(packagePath + ".no_type")
        messageRouter.handle(mockSession!!, "{ route: 'nonTypedMessageHandler' }")
        assertTrue(nonTypedMessageHandlerCalled)
        assertEquals(nonTypedMessageHandlerMessage, "{ route: 'nonTypedMessageHandler' }")
    }

    @Test fun handleNonNullableNoData() {
        assertFalse(nonNullableGreetingHandlerCalled)
        assertNull(nonNullableGreetingHandlerGreeting)
        val messageRouter = MessageRouter(packagePath + ".non_null")
        messageRouter.handle(mockSession!!, "{ route: 'nonNullableGreetingHandler' }")
        assertFalse(nonNullableGreetingHandlerCalled)
        assertNull(nonNullableGreetingHandlerGreeting)
    }

    @Test fun handleNonNullableInvalidData() {
        assertFalse(nonNullableGreetingHandlerCalled)
        assertNull(nonNullableGreetingHandlerGreeting)
        val messageRouter = MessageRouter(packagePath + ".non_null")
        messageRouter.handle(mockSession!!, "{ route: 'nonNullableGreetingHandler', data: {} }")
        assertFalse(nonNullableGreetingHandlerCalled)
        assertNull(nonNullableGreetingHandlerGreeting)
    }

    @Test fun handleNonNullableMessageObject() {
        assertFalse(nonNullableGreetingHandlerCalled)
        assertNull(nonNullableGreetingHandlerGreeting)
        val messageRouter = MessageRouter(packagePath + ".non_null")
        messageRouter.handle(mockSession!!, "{ route: 'nonNullableGreetingHandler', data: { name: 'nameo', exclamation: false } }")
        assertTrue(nonNullableGreetingHandlerCalled)
        assertEquals(NonNullableGreeting("nameo", false), nonNullableGreetingHandlerGreeting)
    }

    @Test fun handleNullableMessageObject() {
        assertFalse(nullableGreetingHandlerCalled)
        assertNull(nullableGreetingHandlerGreeting)
        val messageRouter = MessageRouter(packagePath + ".with_null")
        messageRouter.handle(mockSession!!, "{ route: 'nullableGreetingHandler', data: { name: 'nameo', exclamation: false } }")
        assertTrue(nullableGreetingHandlerCalled)
        assertEquals(Greeting("nameo", false), nullableGreetingHandlerGreeting)
    }

    @Test fun handleNullableMessageObjectEmptyMessage() {
        assertFalse(nullableGreetingHandlerCalled)
        assertNull(nullableGreetingHandlerGreeting)
        val messageRouter = MessageRouter(packagePath + ".with_null")
        messageRouter.handle(mockSession!!, "{ route: 'nullableGreetingHandler', data: {} }")
        assertTrue(nullableGreetingHandlerCalled)
        assertEquals(Greeting(null, null), nullableGreetingHandlerGreeting)
    }

    @Test fun handleNoRoute() {
        assertFalse(nullableGreetingHandlerCalled)
        assertNull(nullableGreetingHandlerGreeting)
        val messageRouter = MessageRouter(packagePath + ".with_null")
        messageRouter.handle(mockSession!!, "{}")
        assertFalse(nullableGreetingHandlerCalled)
        assertNull(nullableGreetingHandlerGreeting)
    }
}
