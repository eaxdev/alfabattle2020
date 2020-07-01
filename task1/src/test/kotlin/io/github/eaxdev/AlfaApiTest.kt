package io.github.eaxdev

import io.github.eaxdev.dto.atmapi.JSONResponseBankATMStatus
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.reactive.function.client.WebClient

@SpringBootTest(classes = [Application::class])
class AlfaApiTest {

    @Autowired
    private lateinit var webClient: WebClient

    @Test
    internal fun callApi() {
        val responseEntity = webClient.get()
            .uri("https://apiws.alfabank.ru/alfabank/alfadevportal/atms/status")
            .header("x-ibm-client-id", "d7d269b5-69b6-4f6d-8c82-40ed848d65b8")
            .exchange()
            .flatMap { it.toEntity(JSONResponseBankATMStatus::class.java) }
            .block()
        print(responseEntity!!.body)
    }
}