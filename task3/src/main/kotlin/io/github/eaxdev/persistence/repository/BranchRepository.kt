package io.github.eaxdev.persistence.repository

import io.github.eaxdev.persistence.model.Branch
import io.github.eaxdev.persistence.model.BranchTable
import org.jetbrains.exposed.sql.DoubleColumnType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class BranchRepository : AbstractRepository<Branch, BranchTable>(BranchTable) {

    //using postgis function
    fun findNearestBranch(lon: Double, lat: Double) = transaction {
        exec(
            """
            SELECT id, title, address, lon, lat,
                   round(ST_DistanceSphere(
                           ST_MakePoint(lon, lat),
                           ST_GeomFromEWKT('SRID=4326;POINT(${lon} ${lat})')
                       ))  AS distance
            FROM branches ORDER BY distance LIMIT 1;
        """) {
            it.next()
            val branch = buildBranch(
                id = it.getInt("id"),
                title = it.getString("title"),
                lat = it.getDouble("lat"),
                lon = it.getDouble("lon"),
                address = it.getString("address")
            )
            branch to it.getInt("distance")
        }

    }

    override fun readRow(r: ResultRow): Branch = with(table) {
        return buildBranch(
            id = r[id],
            title = r[title],
            lat = r[lat],
            lon = r[lon],
            address = r[address]
        )
    }

    private fun buildBranch(id: Int, title: String, lat: Double, lon: Double, address: String): Branch {
        return Branch(
            id = id,
            title = title,
            lat = lat,
            lon = lon,
            address = address
        )
    }
}