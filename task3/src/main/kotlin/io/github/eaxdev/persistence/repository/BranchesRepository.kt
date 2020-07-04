package io.github.eaxdev.persistence.repository

import io.github.eaxdev.persistence.model.Branch
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class BranchRepository {

    fun findById(id: Int) = transaction {
        Branch.findById(id)
    }

    fun findAll() = transaction {
        Branch.all()
    }
}