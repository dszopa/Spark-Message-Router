package io.github.dszopa.message_router.annotation

/**
 * A parameter level annotation specifying that the incoming JSON message
 * should be automatically converted to the parameters object type.
 */
@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class MessageObject
        