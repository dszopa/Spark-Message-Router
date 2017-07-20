package io.github.dszopa.message_router.helper;

import io.github.dszopa.message_router.annotation.MessageController;
import io.github.dszopa.message_router.annotation.MessageObject;
import io.github.dszopa.message_router.annotation.Route;
import org.eclipse.jetty.websocket.api.Session;

@MessageController
public class JavaAnnotatedClass {

    public static int javaAnnotatedClassValue = 0;

    @Route("jTest")
    public void handler(Session user, @MessageObject JavaGreeting greeting) {
        javaAnnotatedClassValue = 1;
    }
}
