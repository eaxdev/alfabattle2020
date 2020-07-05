package io.github.eaxdev.persistence.repository

import io.github.eaxdev.persistence.model.IdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

abstract class AbstractRepository<MODEL : Any, TABLE : IdTable>(open val table: TABLE) {

    protected abstract fun readRow(r: ResultRow): MODEL

    private fun Iterable<ResultRow>.readRows(): List<MODEL> {
        return map { r -> readRow(r) }
    }

    fun findById(id: Int): MODEL? {
        return transaction { table.select { table.id.eq(id) }.readRows().firstOrNull() }
    }

}