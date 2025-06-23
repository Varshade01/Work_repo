package com.maat.cha.core.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MatchViewResponse(
    @Json(name = "LaunchLink")
    val launchLink: String?,
    @Json(name = "matchImg")
    val matchImg: String?
) 