package io.github.eaxdev.service

import io.github.eaxdev.dto.response.BranchDto
import io.github.eaxdev.dto.response.BranchDtoWithPredicting
import io.github.eaxdev.persistence.model.QueueEntity
import io.github.eaxdev.persistence.repository.BranchRepository
import io.github.eaxdev.persistence.repository.QueueRepository
import org.apache.commons.math3.stat.descriptive.rank.Median
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.temporal.ChronoUnit
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong


class BranchNotFound : RuntimeException()

@Service
class BranchService {

    @Autowired
    private lateinit var branchRepository: BranchRepository

    @Autowired
    private lateinit var queueRepository: QueueRepository

    fun getBranchInfo(branchId: Int): BranchDto {
        val branch = branchRepository.findById(branchId) ?: throw BranchNotFound()
        return BranchDto.of(branch)
    }

    fun findNearestBranch(lon: Double, lat: Double): BranchDto {
        val (branch, distance) = branchRepository.findNearestBranch(lon, lat) ?: throw BranchNotFound()
        return BranchDto.of(branch, distance)
    }

    fun predict(branchId: Int, dayOfWeek: Int, hourOfDay: Int): BranchDtoWithPredicting {
        val filteredQueries = transaction {
            queueRepository.findAllByBranchId(branchId)
                .filter { it.data.dayOfWeek.value == dayOfWeek && it.endTimeOfWait.hour == hourOfDay }
                .groupBy { it.branch }.toMap()
        }

        return BranchDtoWithPredicting.of(
            branch = filteredQueries.keys.first(),
            predicting = calculatePredicting(filteredQueries.values.first()),
            dayOfWeek = dayOfWeek,
            hourOfDay = hourOfDay
        )
    }

    private fun calculatePredicting(queue: List<QueueEntity>): Long {
        val timeForWait = queue.map {
            ChronoUnit.SECONDS.between(it.startTimeOfWait, it.endTimeOfWait).toDouble()
        }.toDoubleArray()
        return calculateMedian(timeForWait).roundToLong()
    }

    private fun calculateMedian(values: DoubleArray): Double {
        return Median().evaluate(values)
    }
}
