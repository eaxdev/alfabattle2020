package io.github.eaxdev.service

import io.github.eaxdev.dto.response.BranchDto
import io.github.eaxdev.persistence.repository.BranchRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

class BranchNotFound : RuntimeException()

@Service
class BranchService {

    @Autowired
    private lateinit var branchRepository: BranchRepository

    fun getBranchInfo(branchId: Int): BranchDto {
        val branch = branchRepository.findById(branchId) ?: throw BranchNotFound()
        return BranchDto.of(branch)
    }

    fun findNearestBranch(lon: Double, lat: Double): BranchDto {
        val (branch, distance) = branchRepository.findNearestBranch(lon, lat) ?: throw BranchNotFound()
        return BranchDto.of(branch, distance)
    }
}
