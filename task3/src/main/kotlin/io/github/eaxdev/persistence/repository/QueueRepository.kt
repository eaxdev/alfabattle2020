package io.github.eaxdev.persistence.repository

import io.github.eaxdev.persistence.model.QueueEntity
import io.github.eaxdev.persistence.model.QueueTable
import org.jetbrains.exposed.dao.with
import org.springframework.stereotype.Repository

@Repository
class QueueRepository {

    fun findAllByBranchId(branchId: Int): List<QueueEntity> = QueueEntity
        .find { QueueTable.branch eq branchId }
        .with(QueueEntity::branch)
}