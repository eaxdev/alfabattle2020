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
 * @param atms Список статусов банкоматов
 * @param bankLicense Номер лицензии Альфа-Банка
 */

data class BankATMStatus(
    /* Список статусов банкоматов */
    @JsonProperty("atms")
    val atms: kotlin.Array<ATMStatus>? = null,
    /* Номер лицензии Альфа-Банка */
    @JsonProperty("bankLicense")
    val bankLicense: kotlin.String? = null
)

