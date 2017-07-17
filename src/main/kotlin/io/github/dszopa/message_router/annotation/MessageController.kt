package io.github.dszopa.message_router.annotation

/**
 * A class level annotation that signifies that this class will contain routes
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MessageController