package io.github.dszopa.message_router.router;

import io.github.dszopa.message_router.MessageRouter;
import io.github.dszopa.message_router.exception.ParameterMismatchException;
import io.github.dszopa.message_router.helper.j_no_type.JavaAnnotatedClassNoType;
import io.github.dszopa.message_router.helper.j_with_type.JavaAnnotatedClassWithType;
import io.github.dszopa.message_router.helper.j_with_type.JavaGreeting;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class JavaMessageRouterTest {

    private static String packagePath = "io.github.dszopa.message_router.helper";
    private static String packagePathError = "io.github.dszopa.message_router.exception";
    private Session mockSession;

    @Before
    public void setup() {
        mockSession = mock(Session.class);
        JavaAnnotatedClassNoType.javaAnnotatedClassNoTypeHandlerCalled = false;
        JavaAnnotatedClassNoType.javaAnnotatedClassNoTypeHandlerMessage = "";
        JavaAnnotatedClassWithType.javaAnnotatedClassWithTypeHandlerCalled = false;
        JavaAnnotatedClassWithType.javaAnnotatedClassWithTypeHandlerGreeting = null;
    }

    @Test(expected = ParameterMismatchException.class)
    public void messageRouterInvalidFunctionParams() throws Exception {
        MessageRouter messageRouter = new MessageRouter(packagePathError + ".j_param_mismatch");
    }

    @Test
    public void handleJavaNoType() throws Exception {
        String message = "{ route: 'javaAnnotatedClassNoTypeHandler' }";
        assertFalse(JavaAnnotatedClassNoType.javaAnnotatedClassNoTypeHandlerCalled);
        assertEquals(JavaAnnotatedClassNoType.javaAnnotatedClassNoTypeHandlerMessage, "");
        MessageRouter messageRouter = new MessageRouter(packagePath + ".j_no_type");
        messageRouter.handle(mockSession, message);
        assertTrue(JavaAnnotatedClassNoType.javaAnnotatedClassNoTypeHandlerCalled);
        assertEquals(JavaAnnotatedClassNoType.javaAnnotatedClassNoTypeHandlerMessage, message);
    }

    @Test
    public void handleJavaWithType() throws Exception {
        String message = "{ route: 'javaAnnotatedClassWithTypeHandler', data: { name: 'nameo', exclamation: false } }";
        assertFalse(JavaAnnotatedClassWithType.javaAnnotatedClassWithTypeHandlerCalled);
        assertEquals(JavaAnnotatedClassWithType.javaAnnotatedClassWithTypeHandlerGreeting, null);
        MessageRouter messageRouter = new MessageRouter(packagePath + ".j_with_type");
        messageRouter.handle(mockSession, message);
        assertTrue(JavaAnnotatedClassWithType.javaAnnotatedClassWithTypeHandlerCalled);
        assertEquals(JavaAnnotatedClassWithType.javaAnnotatedClassWithTypeHandlerGreeting, new JavaGreeting("nameo", false));
    }

    @Test
    public void handleJavaWithTypeInvalidData() throws Exception {
        String message = "{ route: 'javaAnnotatedClassWithTypeHandler' }";
        assertFalse(JavaAnnotatedClassWithType.javaAnnotatedClassWithTypeHandlerCalled);
        assertEquals(JavaAnnotatedClassWithType.javaAnnotatedClassWithTypeHandlerGreeting, null);
        MessageRouter messageRouter = new MessageRouter(packagePath + ".j_with_type");
        messageRouter.handle(mockSession, message);
        assertFalse(JavaAnnotatedClassWithType.javaAnnotatedClassWithTypeHandlerCalled);
        assertEquals(JavaAnnotatedClassWithType.javaAnnotatedClassWithTypeHandlerGreeting, null);
    }
}
