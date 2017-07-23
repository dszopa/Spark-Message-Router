package io.github.dszopa.message_router.exception

/**
 * An exception that is throw when trying to assign more than one
 * route to the same MessageHandler.
 */
class DuplicateRouteException(message: String) : Throwable(message)