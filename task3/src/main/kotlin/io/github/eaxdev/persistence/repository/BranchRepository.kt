package io.github.eaxdev.persistence.repository

import io.github.eaxdev.persistence.model.BranchEntity
import io.github.eaxdev.persistence.model.BranchTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class BranchRepository {

    //using postgis function
    fun findNearestBranch(lon: Double, lat: Double) = transaction {
        exec(
            """
            SELECT id, title, lon, lat, address,
                   round(ST_DistanceSphere(
                           ST_MakePoint(lon, lat),
                           ST_GeomFromEWKT('SRID=4326;POINT(${lon} ${lat})')
                       ))  AS distance
            FROM branches ORDER BY distance LIMIT 1;
        """
        ) {
            it.next()
            val branch = BranchEntity.wrapRow(ResultRow.create(it, BranchTable.fields))
            branch to it.getInt("distance")
        }

    }

    fun findById(id: Int): BranchEntity? {
        return transaction { BranchEntity.findById(id) }
    }
}