package io.github.eaxdev.config

import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.util.ResourceUtils
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import reactor.netty.http.client.HttpClient

@Configuration
class Config(private val applicationProperties: ApplicationProperties) {

    @Bean
    fun stompClient(): WebSocketStompClient {
        val stompClient = WebSocketStompClient(StandardWebSocketClient())
        stompClient.apply {
            messageConverter = MappingJackson2MessageConverter()
        }
        return stompClient
    }

    @Bean
    fun webClient(): WebClient {
        val client = HttpClient
            .create()
            //.baseUrl("https://apiws.alfabank.ru/alfabank/alfadevportal")
            .secure { it.sslContext(sslContext()) }

        return WebClient.builder()
            .codecs {
                it.defaultCodecs()
                    .maxInMemorySize(16 * 1024 * 1024)
            }
            .clientConnector(ReactorClientHttpConnector(client))
            .build()
    }

    private fun sslContext(): SslContext {
        val cert = ResourceUtils.getFile(applicationProperties.certificate)
        val key = ResourceUtils.getFile(applicationProperties.privateKey) // PKCS8

        return SslContextBuilder.forClient()
            .keyManager(cert, key)
            .build()
    }

}