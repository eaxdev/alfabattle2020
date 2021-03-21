package io.github.eaxdev.model

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.math.BigDecimal
import java.time.LocalDate

@Document(indexName = "loan")
data class Loan(
    @Id
    val loan: String,
    val docid: String,
    val amount: BigDecimal,
    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd")
    val startdate: LocalDate,
    val period: Int
)