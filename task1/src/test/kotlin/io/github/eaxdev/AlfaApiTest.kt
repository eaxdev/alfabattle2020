package io.github.eaxdev

import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Testcontainers


@Testcontainers
@SpringJUnitConfig
@AutoConfigureMockMvc
@SpringBootTest(classes = [Application::class])
class AlfaApiTest {

    companion object {
        private val websocketContainer = GenericContainer<Nothing>(
            "arpmipg/alfa-battle:task1-websocket"
        )
            .apply { withExposedPorts(8100) }

        @JvmStatic
        @DynamicPropertySource
        fun wsProperties(registry: DynamicPropertyRegistry) {
            websocketContainer.start()
            registry.add("application.alfik-url") { "ws://localhost:${websocketContainer.firstMappedPort}" }
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun getAtmInfo() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/atms/153463")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.city", equalTo("Москва")))
            .andExpect(jsonPath("$.deviceId", equalTo(153463)))
            .andExpect(jsonPath("$.latitude", equalTo("55.6610213")))
            .andExpect(jsonPath("$.longitude", equalTo("37.6309405")))
            .andExpect(jsonPath("$.payments", equalTo(false)))
            .andExpect(jsonPath("$.location", equalTo("Старокаширское ш., 4, корп. 10")))
    }

    @Test
    fun getAtmInfoWithPayments() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/atms/220588")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.city", equalTo("Ломоносов")))
            .andExpect(jsonPath("$.deviceId", equalTo(220588)))
            .andExpect(jsonPath("$.latitude", equalTo("59.903088")))
            .andExpect(jsonPath("$.longitude", equalTo("29.768237")))
            .andExpect(jsonPath("$.payments", equalTo(true)))
            .andExpect(jsonPath("$.location", equalTo("ул. Михайловская,  д. 40/7,  литер А")))
    }

    @Test
    fun getAtmInfoSample() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/atms/153463")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.city", equalTo("Москва")))
            .andExpect(jsonPath("$.deviceId", equalTo(153463)))
            .andExpect(jsonPath("$.latitude", equalTo("55.6610213")))
            .andExpect(jsonPath("$.longitude", equalTo("37.6309405")))
            .andExpect(jsonPath("$.payments", equalTo(false)))
            .andExpect(jsonPath("$.location", equalTo("Старокаширское ш., 4, корп. 10")))
    }

    @Test
    fun getNearestAtmInfoSample() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/atms/nearest?latitude=55.66&longitude=37.63")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.city", equalTo("Москва")))
            .andExpect(jsonPath("$.deviceId", equalTo(153463)))
            .andExpect(jsonPath("$.latitude", equalTo("55.6610213")))
            .andExpect(jsonPath("$.longitude", equalTo("37.6309405")))
            .andExpect(jsonPath("$.payments", equalTo(false)))
            .andExpect(jsonPath("$.location", equalTo("Старокаширское ш., 4, корп. 10")))
    }

    @Test
    fun getNearestAtmInfoSampleWithPayments() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/atms/nearest?latitude=55.66&longitude=37.63&payments=true")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.city", equalTo("Москва")))
            .andExpect(jsonPath("$.deviceId", equalTo(210612)))
            .andExpect(jsonPath("$.latitude", equalTo("55.66442")))
            .andExpect(jsonPath("$.longitude", equalTo("37.628051")))
            .andExpect(jsonPath("$.payments", equalTo(true)))
            .andExpect(jsonPath("$.location", equalTo("Каширское шоссе, д. 14")))
    }

    @Test
    fun getNearestWithPaymentsAtmInfoSample() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/atms/nearest?latitude=55.66&longitude=37.63&payments=true")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.city", equalTo("Москва")))
            .andExpect(jsonPath("$.deviceId", equalTo(210612)))
            .andExpect(jsonPath("$.latitude", equalTo("55.66442")))
            .andExpect(jsonPath("$.longitude", equalTo("37.628051")))
            .andExpect(jsonPath("$.payments", equalTo(true)))
            .andExpect(jsonPath("$.location", equalTo("Каширское шоссе, д. 14")))
    }

    @Test
    fun atmNotFoundSample() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/atms/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status", equalTo("atm not found")))
    }

    @Test
    fun getNearestAtmWithAlfik1Sample() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/atms/nearest-with-alfik?latitude=55.66&longitude=37.63&alfik=300000")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].city", equalTo("Москва")))
            .andExpect(jsonPath("$.[0].deviceId", equalTo(153463)))
            .andExpect(jsonPath("$.[0].latitude", equalTo("55.6610213")))
            .andExpect(jsonPath("$.[0].longitude", equalTo("37.6309405")))
            .andExpect(jsonPath("$.[0].payments", equalTo(false)))
            .andExpect(jsonPath("$.[0].location", equalTo("Старокаширское ш., 4, корп. 10")))
    }

    @Test
    fun getNearestAtmWithAlfik2Sample() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/atms/nearest-with-alfik?latitude=55.66&longitude=37.63&alfik=400000")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.[0].city", equalTo("Москва")))
            .andExpect(jsonPath("$.[0].deviceId", equalTo(153463)))
            .andExpect(jsonPath("$.[0].latitude", equalTo("55.6610213")))
            .andExpect(jsonPath("$.[0].longitude", equalTo("37.6309405")))
            .andExpect(jsonPath("$.[0].payments", equalTo(false)))
            .andExpect(jsonPath("$.[0].location", equalTo("Старокаширское ш., 4, корп. 10")))

            .andExpect(jsonPath("$.[1].city", equalTo("Москва")))
            .andExpect(jsonPath("$.[1].deviceId", equalTo(153465)))
            .andExpect(jsonPath("$.[1].latitude", equalTo("55.6602801")))
            .andExpect(jsonPath("$.[1].longitude", equalTo("37.633823")))
            .andExpect(jsonPath("$.[1].payments", equalTo(false)))
            .andExpect(jsonPath("$.[1].location", equalTo("Каширское ш., 18")))
    }

    //test cases from authors


}