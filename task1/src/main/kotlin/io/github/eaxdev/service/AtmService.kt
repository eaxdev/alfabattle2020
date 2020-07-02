package io.github.eaxdev.service

import io.github.eaxdev.dto.response.AtmResponse
import io.github.eaxdev.provider.AlfaAtmInfoProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


class AtmNotFound : RuntimeException()

@Service
class AtmService {

    @Autowired
    private lateinit var atmInfoProvider: AlfaAtmInfoProvider

    fun getInfoByDeviceId(deviceId: Int): AtmResponse {
        val atmDataMono = atmInfoProvider.getAtmData()
        val block = atmDataMono.block()
        val atmData = block?.data?.atms?.filter { it.deviceId == deviceId }
            ?.first() ?: throw AtmNotFound()
        return AtmResponse.of(atmData)
    }
}