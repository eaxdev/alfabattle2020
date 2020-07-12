package io.github.eaxdev.persistence.model

import io.github.eaxdev.persistence.JavaLocalTimeColumnType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.date
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime


class QueueEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QueueEntity>(QueueTable)

    val data by QueueTable.data
    val startTimeOfWait by QueueTable.startTimeOfWait
    val endTimeOfWait by QueueTable.endTimeOfWait
    val endTimeOfService by QueueTable.endTimeOfService
    val branch by BranchEntity referencedOn QueueTable.branch
}

class BranchEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BranchEntity>(BranchTable)

    val title by BranchTable.title
    val lon by BranchTable.lon
    val lat by BranchTable.lat
    val address by BranchTable.address
}

object QueueTable : IntIdTable("queue_log") {
    var data = date("data")
    var startTimeOfWait = time("start_time_of_wait")
    var endTimeOfWait = time("end_time_of_wait")
    var endTimeOfService = time("end_time_of_service")
    val branch = reference("branches_id", BranchTable)
}

object BranchTable : IntIdTable("branches") {
    var title = varchar("title", 1000)
    var lon = double("lon")
    var lat = double("lat")
    var address = varchar("address", 1000)
}


fun Table.time(name: String): Column<LocalTime> = registerColumn(name, JavaLocalTimeColumnType())

