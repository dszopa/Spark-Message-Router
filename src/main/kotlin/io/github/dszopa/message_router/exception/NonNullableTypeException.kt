package io.github.dszopa.message_router.exception

/**
 * An exception that signals that a Kotlin class had a primary constructor
 * property that was specified as non-nullable. Kotlin class primary constructor
 * properties MUST be nullable.
 */
class NonNullableTypeException(message: String) : Throwable(message)