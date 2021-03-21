package io.github.eaxdev.controller

import io.github.eaxdev.Application
import io.github.eaxdev.readFileAsString
import io.github.eaxdev.repository.PersonRepository
import io.github.eaxdev.service.LoansService
import io.github.eaxdev.service.PersonService
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.skyscreamer.jsonassert.JSONAssert
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.elasticsearch.ElasticsearchContainer
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringJUnitConfig
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = [Application::class])
class LoanControllerTest {

    companion object {
        private val elasticContainer = ElasticsearchContainer(
            "docker.elastic.co/elasticsearch/elasticsearch:7.6.0"
        )

        @JvmStatic
        @DynamicPropertySource
        fun elasticProperties(registry: DynamicPropertyRegistry) {
            elasticContainer.start()
            registry.add("spring.elasticsearch.rest.uris") { elasticContainer.httpHostAddress }
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var personRepository: PersonRepository

    @Autowired
    private lateinit var loanRepository: PersonRepository

    @Autowired
    private lateinit var personService: PersonService

    @Autowired
    private lateinit var loanService: LoansService

    @BeforeAll
    fun setUp() {
        personService.load()
        loanService.load()
    }

    @AfterAll
    fun tearDown() {
        loanRepository.deleteAll()
        personRepository.deleteAll()
    }

    @Test
    fun getPersonSample() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/loans/getPerson/855406656")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response

        val actual = response.contentAsString
        JSONAssert.assertEquals(readFileAsString("expected/Person1.json"), actual, JSONCompareMode.STRICT)
    }

    @Test
    fun getLoanSample() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/loans/getLoan/692826")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response

        val actual = response.contentAsString
        JSONAssert.assertEquals(readFileAsString("expected/Loan1.json"), actual, JSONCompareMode.STRICT)
    }

    @Test
    fun getLoanHistorySample() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/loans/creditHistory/737767072")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response

        val actual = response.contentAsString
        JSONAssert.assertEquals(readFileAsString("expected/LoanHistory1.json"), actual, JSONCompareMode.STRICT)
    }

    @Test
    fun getCreditClosed() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/loans/creditClosed")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response

        val actual = response.contentAsString
        JSONAssert.assertEquals(readFileAsString("expected/CreditClosed1.json"), actual, JSONCompareMode.STRICT)
    }

    @Test
    fun loansSortByPersonBirthday() {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get("/loans/loansSortByPersonBirthday")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response

        val actual = response.contentAsString
        JSONAssert.assertEquals(readFileAsString("expected/LoansSortByPersonBirthday1.json"), actual, JSONCompareMode.STRICT)
    }
}