/**
 * Api Documentation
 * Api Documentation
 *
 * The version of the OpenAPI document: 1.0
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package io.github.eaxdev.dto.response


import com.fasterxml.jackson.annotation.JsonProperty
import io.github.eaxdev.persistence.model.Branch

/**
 *
 * @param address
 * @param id
 * @param lat
 * @param lon
 * @param title
 */

data class BranchDto(
    @JsonProperty("address")
    val address: kotlin.String? = null,
    @JsonProperty("id")
    val id: kotlin.Long? = null,
    @JsonProperty("lat")
    val lat: kotlin.Double? = null,
    @JsonProperty("lon")
    val lon: kotlin.Double? = null,
    @JsonProperty("title")
    val title: kotlin.String? = null
) {
    companion object {
        fun of(branch: Branch): BranchDto {
            return BranchDto(
                address = branch.address,
                id = branch.id.value.toLong(),
                lat = branch.lat,
                lon = branch.lon,
                title = branch.title
            )
        }
    }
}
