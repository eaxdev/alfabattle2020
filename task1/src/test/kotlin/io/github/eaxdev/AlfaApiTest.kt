package io.github.eaxdev

import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringJUnitConfig
@AutoConfigureMockMvc
@SpringBootTest(classes = [Application::class])
class AlfaApiTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun getAtmInfo() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/atms/153475")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.city", equalTo("Москва")))
            .andExpect(jsonPath("$.deviceId", equalTo(153475)))
            .andExpect(jsonPath("$.latitude", equalTo("55.77813")))
            .andExpect(jsonPath("$.longitude", equalTo("37.666618")))
            .andExpect(jsonPath("$.payments", equalTo(false)))
            .andExpect(jsonPath("$.location", equalTo("Нижняя Красносельская ул., 6, стр. 1")))
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
}