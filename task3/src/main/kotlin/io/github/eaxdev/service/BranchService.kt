package io.github.eaxdev.service

import io.github.eaxdev.dto.response.BranchDto
import io.github.eaxdev.dto.response.BranchDtoWithPredicting
import io.github.eaxdev.persistence.repository.BranchRepository
import io.github.eaxdev.persistence.repository.QueueRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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
        val allByBranches = queueRepository.findAllByBranchId(branchId)
        return BranchDtoWithPredicting()
    }
}
