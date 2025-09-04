package com.munity.sheetsautomator.core.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorInfo(
    val error: String,
    @SerialName("error_description") val errorDescription: String,
)
