/**
 * Сервис проверки статуса банкоматов
 * Сервис, возвращающий информацию о банкоматах Альфа-Банка
 *
 * The version of the OpenAPI document: 1.0.0
 * Contact: apisupport@alfabank.ru
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package io.github.eaxdev.dto.atmapi


import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 * @param email Адрес электронной почты
 * @param other Иные контакты поддержки
 * @param phone Телефон
 */

data class SupportInfo(
    /* Адрес электронной почты */
    @JsonProperty("email")
    val email: kotlin.String? = null,
    /* Иные контакты поддержки */
    @JsonProperty("other")
    val other: kotlin.String? = null,
    /* Телефон */
    @JsonProperty("phone")
    val phone: kotlin.String? = null
)

