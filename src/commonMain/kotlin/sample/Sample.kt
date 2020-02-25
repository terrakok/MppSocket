package sample

expect fun randomUUID(): String

fun run() {
    val socket = AppSocket("wss://echo.websocket.org")

    var receivedCounter = 0
    socket.messageListener = { msg ->
        println("Received: $msg")
        receivedCounter++
        if (receivedCounter == 3) socket.disconnect()
    }
    socket.stateListener = { state ->
        println("STATE = $state")
        when (state) {
            AppSocket.State.CONNECTED -> {
                repeat(3) {
                    val msg = "MSG#${randomUUID()}"
                    socket.send(msg)
                    println("Sent: $msg")
                }
            }
        }
    }

    socket.connect()
}