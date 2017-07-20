package io.github.dszopa.message_router.helper;

import io.github.dszopa.message_router.annotation.MessageController;
import io.github.dszopa.message_router.annotation.MessageObject;
import io.github.dszopa.message_router.annotation.Route;
import org.eclipse.jetty.websocket.api.Session;

@MessageController
public class JavaAnnotatedClassWithCustomParam {

    public static int javaAnnotatedClassValue = 0;

    @Route("jCustomTest")
    public void handler(Session user, @MessageObject JavaGreeting greeting) {
        javaAnnotatedClassValue = (greeting.getName().equals("nameo") && greeting.getExclamation().equals(false)) ? 1 : 0;
    }
}
