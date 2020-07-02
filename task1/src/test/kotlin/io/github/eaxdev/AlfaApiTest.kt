package io.github.eaxdev

import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

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
}