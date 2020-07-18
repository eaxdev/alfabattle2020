package io.github.eaxdev.provider

import io.github.eaxdev.logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.stomp.*
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.beans.ConstructorProperties
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

data class AlfikRequest(val deviceId: Int)
data class AlfikResponse @ConstructorProperties("deviceId", "alfik") constructor(
    val deviceId: Int,
    val alfik: Long
)

@Component
class AlfikInfoProvider {

    private val log = logger<AlfikInfoProvider>()

    @Autowired
    private lateinit var stompClient: WebSocketStompClient

    fun getAlfikByDeviceId(deviceId: Int): AlfikResponse {
        val request = AlfikRequest(deviceId)

        val url = "ws://localhost:8100"
        val topic = "/topic/alfik"

        val response = AtomicReference<AlfikResponse>()

        log.info("Subscribing to {} topic", topic)

        val latch = CountDownLatch(1)
        val session = stompClient.connect(url, Handler {
            log.info("HandleFrame: {}", it)
            response.set(it)
            latch.countDown()
        }).get()

        try {
            log.info("Sending request message: {}", request)
            while (true) {
                try {
                    session.send("/", request)
                    break
                } catch (e: java.lang.Exception) {
                    //log.error("", e)
                    Thread.sleep(1000)
                }
            }
            return if (latch.await(10, TimeUnit.SECONDS)) {
                val result = response.get()
                checkNotNull(result)
                result
            } else {
                throw RuntimeException("Response not received")
            }

        } finally {
            if (session != null && session.isConnected) {
                session.disconnect()
                log.info("Close session")
            }
        }

    }

    private inner class Handler(private val afterHandle: (AlfikResponse) -> Unit) : StompSessionHandler {
        override fun handleException(
            session: StompSession,
            command: StompCommand?,
            headers: StompHeaders,
            payload: ByteArray,
            exception: Throwable
        ) {
            log.error("Got an exception", exception)
        }

        override fun handleTransportError(session: StompSession, exception: Throwable) {
            log.error("Got an exception", exception)
        }

        override fun handleFrame(headers: StompHeaders, payload: Any?) {
            log.debug("Handle frame: {}", payload)
            if (payload != null) {
                afterHandle.invoke(payload as AlfikResponse)
            }
        }

        override fun afterConnected(session: StompSession, connectedHeaders: StompHeaders) {
            val topic = "/topic/alfik"
            log.info("Subscribe topic: {}", topic)
            session.subscribe(topic, this)
        }

        override fun getPayloadType(headers: StompHeaders): Type {
            return AlfikResponse::class.java
        }

    }
}