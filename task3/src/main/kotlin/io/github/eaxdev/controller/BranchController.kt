package io.github.eaxdev.controller

import io.github.eaxdev.dto.response.BranchDto
import io.github.eaxdev.service.BranchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Positive

@RestController
@RequestMapping("/branches")
class BranchController {

    @Autowired
    private lateinit var branchService: BranchService

    @GetMapping("/{id}")
    fun getBranchInfo(@Positive @PathVariable("id") branchId: Int): ResponseEntity<BranchDto> {
        return ResponseEntity.ok(branchService.getBranchInfo(branchId))
    }

    @GetMapping
    fun findNearestBranch(@Positive @RequestParam("lat") lat: Double,
                         @Positive @RequestParam("lon") lon: Double) : ResponseEntity<BranchDto> {
        return ResponseEntity.ok(branchService.findNearestBranch(lon, lat))
    }

}