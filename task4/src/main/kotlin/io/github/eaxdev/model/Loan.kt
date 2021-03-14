package io.github.eaxdev.model

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "loan")
data class Loan(
    @Id
    val loan: String,
    val docid: String,
    val amount: Double,
    val startdate: String,
    val period: Int
)