package io.github.eaxdev.persistence.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

data class Branch(
    val id: Int,
    val title: String,
    val lat: Double,
    val lon: Double,
    val address: String
)

object BranchTable : IdTable("branches") {
    var title = varchar("title", 1000)
    var lon = double("lon")
    var lat = double("lat")
    var address = varchar("address", 1000)
}

abstract class IdTable(name: String) : Table(name) {
    val id: Column<Int> = integer("id").autoIncrement()
}

