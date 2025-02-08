package com.munity.sheetsautomator.core.data.model.gsheet

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorInfo(
    val error: String,
    @SerialName("error_description") val errorDescription: String,
)
