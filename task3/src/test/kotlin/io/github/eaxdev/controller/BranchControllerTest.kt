package io.github.eaxdev.controller

import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.spring.api.DBRider
import io.github.eaxdev.Application
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@DBRider
@SpringJUnitConfig
@AutoConfigureMockMvc
@SpringBootTest(classes = [Application::class])
class BranchControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DataSet("branches.yml")
    fun getBranchByIdSample() {
        mockMvc.perform(
            get("/branches/612")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", equalTo(612)))
            .andExpect(jsonPath("$.title", equalTo("Мясницкий")))
            .andExpect(jsonPath("$.lon", equalTo(37.6329)))
            .andExpect(jsonPath("$.lat", equalTo(55.7621)))
            .andExpect(jsonPath("$.address", equalTo("Мясницкая ул., 13, стр. 1")))
    }

    @Test
    @DataSet("branches.yml")
    fun getNearestBranchByIdSample() {
        mockMvc.perform(
            get("/branches?lat=55.773284&lon=37.624125")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", equalTo(631)))
            .andExpect(jsonPath("$.title", equalTo("Цветной Бульвар")))
            .andExpect(jsonPath("$.lon", equalTo(37.6227)))
            .andExpect(jsonPath("$.lat", equalTo(55.7695)))
            .andExpect(jsonPath("$.address", equalTo("Цветной бул., 16/1")))
            .andExpect(jsonPath("$.distance", equalTo(430)))
    }

    @Test
    fun branchNotFoundSample() {
        mockMvc.perform(
            get("/branches/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status", equalTo("branch not found")))
    }

    @Test
    @Disabled
    @DataSet("branches.yml", "queue_log.json")
    fun predictSample() {
        mockMvc.perform(
            get("/branches/612/predict?dayOfWeek=1&hourOfDay=14")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id", equalTo(612)))
            .andExpect(jsonPath("$.title", equalTo("Мясницкий")))
            .andExpect(jsonPath("$.lon", equalTo(37.6329)))
            .andExpect(jsonPath("$.lat", equalTo(55.7621)))
            .andExpect(jsonPath("$.address", equalTo("Мясницкая ул., 13, стр. 1")))
            .andExpect(jsonPath("$.dayOfWeek", equalTo(1)))
            .andExpect(jsonPath("$.hourOfDay", equalTo(14)))
            .andExpect(jsonPath("$.predicting", equalTo(117)))
    }
}