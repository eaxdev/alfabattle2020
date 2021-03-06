package io.github.eaxdev.controller

import io.github.eaxdev.dto.response.AtmResponse
import io.github.eaxdev.service.AtmService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Positive

@RestController
class AtmController {

    @Autowired
    private lateinit var atmService: AtmService

    @GetMapping("/atms/{deviceId}")
    fun getDataByDeviceId(@Positive @PathVariable("deviceId") deviceId: Int): ResponseEntity<AtmResponse> {
        return ResponseEntity.ok(atmService.getInfoByDeviceId(deviceId))
    }

    @GetMapping("/atms/nearest")
    fun getNearestAtm(
        @Positive @RequestParam("latitude") latitude: String,
        @Positive @RequestParam("longitude") longitude: String,
        @RequestParam("payments") payments: Boolean = false
    ): ResponseEntity<AtmResponse> {
        return ResponseEntity.ok(atmService.getNearestAtm(latitude, longitude, payments))
    }

    @GetMapping("/atms/nearest-with-alfik")
    fun getNearestAtmWithAlfik(
        @Positive @RequestParam("latitude") latitude: String,
        @Positive @RequestParam("longitude") longitude: String,
        @Positive @RequestParam("alfik") alfik: Int
    ): ResponseEntity<List<AtmResponse>> {
        return ResponseEntity.ok(atmService.getNearestAtmWithAlfik(latitude, longitude, alfik))
    }

}