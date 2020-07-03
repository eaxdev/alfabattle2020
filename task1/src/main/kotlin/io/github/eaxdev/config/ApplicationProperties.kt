package io.github.eaxdev.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component
import javax.validation.constraints.NotEmpty

@ConstructorBinding
@ConfigurationProperties(prefix = "application")
data class ApplicationProperties(
    @NotEmpty val alfaClientId: String,
    @NotEmpty val alfaAtmInfoUrl: String,
    @NotEmpty val privateKey: String,
    @NotEmpty val certificate: String
)