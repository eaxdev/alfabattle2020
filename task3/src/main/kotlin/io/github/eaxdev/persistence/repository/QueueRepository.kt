package io.github.eaxdev.persistence.repository

import io.github.eaxdev.persistence.model.Queue
import io.github.eaxdev.persistence.model.QueueTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class QueueRepository : AbstractRepository<Queue, QueueTable>(QueueTable) {

    fun findAllByBranchId(branchId: Int): List<Queue> = transaction {
        QueueTable.select { QueueTable.branchId eq branchId }.readRows()
    }

    override fun readRow(r: ResultRow): Queue = with(table) {
        return Queue(
            id = r[id],
            data = r[data],
            startTimeOfWait = r[startTimeOfWait],
            endTimeOfWait = r[endTimeOfWait],
            endTimeOfService = r[endTimeOfService],
            branchId = r[branchId]
        )
    }


}