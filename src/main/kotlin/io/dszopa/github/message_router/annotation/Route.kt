package io.dszopa.github.message_router.annotation

// TODO: add support for route being a regex string
/**
 * A funtion-level annotation that specifies that the function should be ran when
 *  a socket request in JSON format with a "route" value matches the given value
 *  for the route annotation.
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Route(val value: String)
