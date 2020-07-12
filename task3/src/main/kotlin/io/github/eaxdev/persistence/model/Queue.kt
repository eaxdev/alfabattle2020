package io.github.eaxdev.persistence.model

import io.github.eaxdev.persistence.JavaLocalTimeColumnType
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

data class Queue(
    val id: Int,
    val branchId: Int,
    val data: LocalDate,
    val startTimeOfWait: LocalTime,
    val endTimeOfWait: LocalTime,
    val endTimeOfService: LocalTime
)

object QueueTable : IdTable("queue_log") {
    var data = date("data")
    var startTimeOfWait = time("start_time_of_wait")
    var endTimeOfWait = time("end_time_of_wait")
    var endTimeOfService = time("end_time_of_service")
    val branchId = (integer("branches_id") references BranchTable.id)
}

fun Table.time(name: String): Column<LocalTime> = registerColumn(name, JavaLocalTimeColumnType())

