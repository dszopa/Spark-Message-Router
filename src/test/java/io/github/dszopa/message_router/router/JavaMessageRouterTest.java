package io.github.dszopa.message_router.router;

import io.github.dszopa.message_router.MessageRouter;
import io.github.dszopa.message_router.exception.ParameterMissmatchException;
import io.github.dszopa.message_router.helper.JavaAnnotatedClass;
import io.github.dszopa.message_router.helper.JavaAnnotatedClassWithCustomParam;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class JavaMessageRouterTest {

    private static String packagePath = "io.github.dszopa.message_router.helper";
    private static String packagePrefixError = "io.github.dszopa.message_router.exception";
    private MessageRouter messageRouter;
    private Session mockSession;

    @Before
    public void setup() {
        messageRouter = new MessageRouter(packagePath);
        mockSession = mock(Session.class);
    }

    @Test
    public void TestMessageRouter() throws Exception {
        assertTrue(JavaAnnotatedClass.javaAnnotatedClassValue == 0);
        messageRouter.handle(mockSession, "{ route: 'jTest' }");
        assertTrue(JavaAnnotatedClass.javaAnnotatedClassValue == 1);
    }

    @Test
    public void TestCustomMessageRouter() throws Exception {
        assertTrue(JavaAnnotatedClassWithCustomParam.javaAnnotatedClassValue == 0);
        messageRouter.handle(mockSession, "{ route: 'jCustomTest', name: 'nameo', exclamation: 'false' }");
        assertTrue(JavaAnnotatedClassWithCustomParam.javaAnnotatedClassValue == 1);
    }

    @Test(expected = ParameterMissmatchException.class)
    public void TestCustomMessageRouterInvalidParameters() throws Exception {
        messageRouter = new MessageRouter(packagePrefixError + ".param_mismatch");
    }
}
