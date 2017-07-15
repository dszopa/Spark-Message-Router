package io.dszopa.github.message_router.annotation

// TODO: add support for route being a regex string
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Route(val value: String)
