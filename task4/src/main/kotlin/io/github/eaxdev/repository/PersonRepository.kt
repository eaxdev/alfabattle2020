package io.github.eaxdev.repository

import io.github.eaxdev.model.Person
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface PersonRepository : ElasticsearchRepository<Person, String> {

    fun findByDocid(docid: String): Person?
}