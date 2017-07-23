package io.github.dszopa.message_router.annotation

/**
 * A funtion-level annotation that specifies that the function should be ran when
 *  a socket request in JSON format with a "route" value matches the given value
 *  for the route annotation.
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Route(val value: String)
