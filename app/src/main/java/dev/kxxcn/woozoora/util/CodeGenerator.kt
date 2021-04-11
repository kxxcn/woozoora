package dev.kxxcn.woozoora.util

object CodeGenerator {

    private const val STREAM_SIZE = 20L

    private val source = ('A'..'Z') + ('0'..'9') + ('a'..'z') + ('0'..'9')

    fun random(): String {
        return (1..STREAM_SIZE)
            .map { source.random() }
            .joinToString("")
    }
}