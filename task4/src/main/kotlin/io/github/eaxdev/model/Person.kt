package io.github.eaxdev.model

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "person")
data class Person(
    @Id
    val id: String,
    val docid: String,
    val fio: String,
    val birthday: String,
    val salary: Double,
    val gender: String
)
