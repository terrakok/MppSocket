package sample

import java.util.*

actual fun randomUUID() = UUID.randomUUID().toString()

fun main() {
    run()

    while (true) {}
}