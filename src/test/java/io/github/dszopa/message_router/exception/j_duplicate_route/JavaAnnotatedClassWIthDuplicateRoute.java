package io.github.dszopa.message_router.exception.j_duplicate_route;

import io.github.dszopa.message_router.annotation.MessageController;
import io.github.dszopa.message_router.annotation.Route;
import org.eclipse.jetty.websocket.api.Session;

@MessageController
public class JavaAnnotatedClassWIthDuplicateRoute {

    @Route("duplicateRouteHandler")
    public void duplicateRouteHandler1(Session user, String message) {}

    @Route("duplicateRouteHandler")
    public void duplicateRouteHandler2(Session user, String message) {}
}