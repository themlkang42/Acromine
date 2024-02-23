package com.mlkang.albertsonsacromine

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LongFormNetworkData(
    @Json(name = "sf") val acronym: String,
    @Json(name = "lfs") val longForms: List<LongForm>
)

@JsonClass(generateAdapter = true)
data class LongForm(
    @Json(name="lf") val text: String,
    @Json(name="freq") val frequency: Int,
    @Json(name="since") val sinceYear: Int,
    @Json(name="vars") val variations: List<Variation>
)

@JsonClass(generateAdapter = true)
data class Variation(
    @Json(name="lf") val text: String,
    @Json(name="freq") val frequency: Int,
    @Json(name="since") val sinceYear: Int
)
