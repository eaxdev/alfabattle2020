package io.github.eaxdev.config

import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
class Config {

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
        val cert = Config::class.java.classLoader.getResourceAsStream("apidevelopers.cer")
        val key = Config::class.java.classLoader.getResourceAsStream("apidevelopers.key")

        return SslContextBuilder.forClient()
            .keyManager(cert, key)
            .build()
    }

}