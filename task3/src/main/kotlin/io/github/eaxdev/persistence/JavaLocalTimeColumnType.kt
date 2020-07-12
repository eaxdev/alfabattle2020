package io.github.eaxdev.persistence

import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.IDateColumnType
import org.jetbrains.exposed.sql.vendors.DataTypeProvider
import org.jetbrains.exposed.sql.vendors.currentDialect
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

private val DEFAULT_TIME_STRING_FORMATTER by lazy {
    DateTimeFormatter.ISO_LOCAL_TIME.withLocale(Locale.ROOT)
        .withZone(ZoneId.systemDefault())
}

fun DataTypeProvider.timeType(): String = "DATE"

class JavaLocalTimeColumnType : ColumnType(), IDateColumnType {

    override fun sqlType(): String = currentDialect.dataTypeProvider.timeType()

    override fun nonNullValueToString(value: Any): String {
        val instant = when (value) {
            is String -> return value
            is LocalTime -> Instant.from(LocalDate.MIN.atTime(value).atZone(ZoneId.systemDefault()))
            is java.sql.Date -> Instant.ofEpochMilli(value.time)
            is java.sql.Timestamp -> Instant.ofEpochMilli(value.time)
            is java.sql.Time -> Instant.from(value.toLocalTime())
            else -> error("Unexpected value: $value of ${value::class.qualifiedName}")
        }

        return "'${DEFAULT_TIME_STRING_FORMATTER.format(instant)}'"
    }

    override fun valueFromDB(value: Any): Any = when (value) {
        is LocalTime -> value
        is java.sql.Time -> value.toLocalTime()
        is java.sql.Date -> value.toLocalDate().atStartOfDay()
        is java.sql.Timestamp -> value.toLocalDateTime()
        is Int -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value.toLong()), ZoneId.systemDefault())
        is Long -> LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
        is String -> value
        else -> LocalTime.parse(value.toString())
    }

    override fun notNullValueToDB(value: Any): Any = when (value) {
        is LocalTime -> DEFAULT_TIME_STRING_FORMATTER.format(value)
        else -> value
    }

    companion object {
        internal val INSTANCE = JavaLocalTimeColumnType()
    }
}