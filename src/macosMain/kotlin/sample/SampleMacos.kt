package sample

import platform.Foundation.*

actual fun randomUUID(): String = NSUUID().UUIDString()

fun main() {
    run()
    while (true) {
        NSRunLoop.currentRunLoop.runUntilDate(NSDate.dateWithTimeIntervalSinceNow(1.0))
    }
}
