package io.dszopa.github.message_router

import org.eclipse.jetty.websocket.api.Session
import org.json.JSONObject

interface  RouteHandler {

    // TODO: figure out if I can make this message into a java object rather than a json one...
    fun handle(user: Session, message: JSONObject)
}