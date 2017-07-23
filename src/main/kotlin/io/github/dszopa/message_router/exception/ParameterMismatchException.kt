package io.github.dszopa.message_router.exception

/**
 * An exception that indicates that a method annotated with @Route
 * did not have its first parameter as a Session type, or the second parameter
 * was not a String and was not annotated with @MessageObject
 */
class ParameterMismatchException(message: String) : Throwable(message)