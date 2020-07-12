package io.github.eaxdev.persistence.model

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

abstract class IdTable(name: String) : Table(name) {
    val id: Column<Int> = integer("id").autoIncrement()
}