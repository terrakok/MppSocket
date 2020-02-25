package sample

class AppSocket(url: String) {
    private val ws = PlatformSocket(url)

    var socketError: Throwable? = null
        private set

    var currentState: State = State.CLOSED
        private set(value) {
            field = value
            stateListener?.invoke(value)
        }

    var stateListener: ((State) -> Unit)? = null
        set(value) {
            field = value
            value?.invoke(currentState)
        }

    var messageListener: ((msg: String) -> Unit)? = null

    fun connect() {
        if (currentState != State.CLOSED) {
            throw IllegalStateException("Socket has alive!")
        }

        socketError = null
        currentState = State.CONNECTING
        ws.openSocket(socketListener)
    }

    fun disconnect() {
        if (currentState != State.CLOSED) {
            currentState = State.CLOSING
            ws.closeSocket(1000, "User closed connection")
        }
    }

    fun send(msg: String) {
        if (currentState != State.CONNECTED) throw IllegalStateException("Socket doesn't connect")
        ws.sendMessage(msg)
    }

    private val socketListener = object : PlatformSocketListener {
        override fun onOpen() {
            currentState = State.CONNECTED
        }

        override fun onFailure(t: Throwable) {
            socketError = t
            currentState = State.CLOSED
        }

        override fun onMessage(msg: String) {
            messageListener?.invoke(msg)
        }

        override fun onClosing(code: Int, reason: String) {
            currentState = State.CLOSING
        }

        override fun onClosed(code: Int, reason: String) {
            currentState = State.CLOSED
        }
    }

    enum class State {
        CONNECTING,
        CONNECTED,
        CLOSING,
        CLOSED
    }
}