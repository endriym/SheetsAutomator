package com.munity.sheetsautomator.core.data.remote.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Spreadsheet(
    val spreadsheetId: String,
    val sheets: List<Sheet>,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Sheet(
    val properties: SheetProperties,
) {
    @Serializable
    @JsonIgnoreUnknownKeys
    data class SheetProperties(
        val sheetId: String,
        val title: String,
        )
}


