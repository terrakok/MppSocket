package sample

import org.w3c.dom.WebSocket

internal actual class PlatformSocket actual constructor(url: String) {
    private val socketEndpoint = url
    private var webSocket: WebSocket? = null

    actual fun openSocket(listener: PlatformSocketListener) {
        webSocket = WebSocket(socketEndpoint).apply {
            onopen = { listener.onOpen() }
            onerror = { event -> listener.onFailure(Throwable(event.type)) }
            onmessage = { event -> listener.onMessage(event.data.toString()) }
            onclose = { event -> listener.onClosed(0, event.toString()) }
        }
    }

    actual fun closeSocket(code: Int, reason: String) {
        webSocket?.close(code.toShort(), reason)
        webSocket = null
    }

    actual fun sendMessage(msg: String) {
        webSocket?.send(msg)
    }
}