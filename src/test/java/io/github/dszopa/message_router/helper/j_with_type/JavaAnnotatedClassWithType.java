package io.github.dszopa.message_router.helper.j_with_type;

import io.github.dszopa.message_router.annotation.MessageController;
import io.github.dszopa.message_router.annotation.MessageObject;
import io.github.dszopa.message_router.annotation.Route;
import org.eclipse.jetty.websocket.api.Session;

@MessageController
public class JavaAnnotatedClassWithType {

    public static boolean javaAnnotatedClassWithTypeHandlerCalled = false;
    public static JavaGreeting javaAnnotatedClassWithTypeHandlerGreeting = null;

    @Route("javaAnnotatedClassWithTypeHandler")
    public void javaAnnotatedClassWithTypeHandler(Session user, @MessageObject JavaGreeting greeting) {
        javaAnnotatedClassWithTypeHandlerCalled = true;
        javaAnnotatedClassWithTypeHandlerGreeting = greeting;
    }
}
