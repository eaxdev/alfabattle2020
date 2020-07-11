package io.github.eaxdev.service

import io.github.eaxdev.dto.atmapi.ATMDetails
import io.github.eaxdev.dto.atmapi.JSONResponseBankATMDetails
import io.github.eaxdev.dto.response.AtmResponse
import io.github.eaxdev.provider.AlfaAtmInfoProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.math.pow
import kotlin.math.sqrt


class AtmNotFound : RuntimeException()

@Service
class AtmService {

    @Autowired
    private lateinit var atmInfoProvider: AlfaAtmInfoProvider

    fun getInfoByDeviceId(deviceId: Int): AtmResponse {
        val atmDataMono = atmInfoProvider.getAtmData()
        val block = atmDataMono.block()
        val atmData = block?.data?.atms?.find { it.deviceId == deviceId } ?: throw AtmNotFound()
        return AtmResponse.of(atmData)
    }

    fun getNearestAtm(latitude: String, longitude: String, withPaymentsOnly: Boolean): AtmResponse {
        val atmDataMono = atmInfoProvider.getAtmData()
        val response = atmDataMono.block()
        val atmData =
            if (withPaymentsOnly) findNearestWithPaymentsOnly(response, longitude, latitude)
            else findNearest(response, longitude, latitude)
        return AtmResponse.of(atmData)
    }

    private fun findNearest(
        response: JSONResponseBankATMDetails?,
        longitude: String,
        latitude: String
    ): ATMDetails {
        return response?.data?.atms?.filter {
            it.coordinates?.latitude?.contains(latitude) ?: false
                    && it.coordinates?.longitude?.contains(longitude) ?: false
        }?.minBy { it.calculateDistance(latitude, longitude) } ?: throw AtmNotFound()
    }

    private fun findNearestWithPaymentsOnly(
        response: JSONResponseBankATMDetails?,
        longitude: String,
        latitude: String
    ): ATMDetails {
        return response?.data?.atms?.filter {
            it.services?.payments == "Y"
        }?.minBy { it.calculateDistance(latitude, longitude) } ?: throw AtmNotFound()
    }

    companion object {
        private fun ATMDetails.calculateDistance(latitude: String, longitude: String): Double {
            val atmLatitude = coordinates?.latitude?.toDouble() ?: return Double.NEGATIVE_INFINITY
            val atmLongitude = coordinates.longitude?.toDouble() ?: return Double.NEGATIVE_INFINITY

            val x = atmLatitude.minus(latitude.toDouble()).pow(2)
            val y = atmLongitude.minus(longitude.toDouble()).pow(2)

            return sqrt(x.plus(y))
        }
    }
}