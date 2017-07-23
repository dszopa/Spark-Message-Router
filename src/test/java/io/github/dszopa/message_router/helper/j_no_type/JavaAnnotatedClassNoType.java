package io.github.dszopa.message_router.helper.j_no_type;

import io.github.dszopa.message_router.annotation.MessageController;
import io.github.dszopa.message_router.annotation.Route;
import org.eclipse.jetty.websocket.api.Session;

@MessageController
public class JavaAnnotatedClassNoType {

    public static boolean javaAnnotatedClassNoTypeHandlerCalled = false;
    public static String javaAnnotatedClassNoTypeHandlerMessage = "";

    @Route("javaAnnotatedClassNoTypeHandler")
    public void javaAnnotatedClassNoTypeHandler(Session user, String message) {
        javaAnnotatedClassNoTypeHandlerCalled = true;
        javaAnnotatedClassNoTypeHandlerMessage = message;
    }
}
