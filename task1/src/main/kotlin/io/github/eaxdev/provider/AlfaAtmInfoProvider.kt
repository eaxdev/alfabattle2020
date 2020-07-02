package io.github.eaxdev.provider

import io.github.eaxdev.dto.atmapi.BankATMDetails
import io.github.eaxdev.dto.atmapi.JSONResponseBankATMDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class AlfaAtmInfoProvider {

    @Autowired
    private lateinit var webClient: WebClient

    fun getAtmData(): Mono<JSONResponseBankATMDetails> {
        return webClient.get()
            .uri("https://apiws.alfabank.ru/alfabank/alfadevportal/atm-service/atms")
            .header("x-ibm-client-id", "d7d269b5-69b6-4f6d-8c82-40ed848d65b8")
            .exchange()
            .flatMap { it.bodyToMono(JSONResponseBankATMDetails::class.java) }
    }

}