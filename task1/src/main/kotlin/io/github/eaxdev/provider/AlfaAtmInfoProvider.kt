package io.github.eaxdev.provider

import io.github.eaxdev.config.ApplicationProperties
import io.github.eaxdev.dto.atmapi.JSONResponseBankATMDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class AlfaAtmInfoProvider(private val applicationProperties: ApplicationProperties) {

    @Autowired
    private lateinit var webClient: WebClient

    fun getAtmData(): Mono<JSONResponseBankATMDetails> {
        return webClient.get()
            .uri(applicationProperties.alfaAtmInfoUrl)
            .header("x-ibm-client-id", applicationProperties.alfaClientId)
            .exchange()
            .flatMap { it.bodyToMono(JSONResponseBankATMDetails::class.java) }
    }

}