package io.github.eaxdev.persistence.model

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.springframework.stereotype.Repository

class Branch(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Branch>(BranchTable)
    var title by BranchTable.title
    var lon by BranchTable.lon
    var lat by BranchTable.lat
    var address by BranchTable.address
}

object BranchTable : IntIdTable("branches") {
    var title = varchar("title", 1000)
    var lon = double("lon")
    var lat = double("lat")
    var address = varchar("address", 1000)
}


