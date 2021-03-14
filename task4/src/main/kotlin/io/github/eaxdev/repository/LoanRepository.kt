package io.github.eaxdev.repository

import io.github.eaxdev.model.Loan
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface LoanRepository : ElasticsearchRepository<Loan, String> {

    fun findAllByDocid(docId: String): List<Loan>
}