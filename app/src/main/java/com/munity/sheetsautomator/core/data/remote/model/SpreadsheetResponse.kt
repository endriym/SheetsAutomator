package com.munity.sheetsautomator.core.data.remote.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlinx.serialization.json.JsonNames

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class SpreadsheetResponse(
    val spreadsheetId: String,
    @SerialName("sheets") val sheetResponses: List<SheetResponse>,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class SheetResponse(
    val properties: SheetProperties,
) {
    @Serializable
    @JsonIgnoreUnknownKeys
    data class SheetProperties(
        val sheetId: String,
        val title: String,
        )
}


