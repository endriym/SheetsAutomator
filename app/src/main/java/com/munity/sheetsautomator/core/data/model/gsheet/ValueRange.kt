package com.munity.sheetsautomator.core.data.model.gsheet

import kotlinx.serialization.Serializable

@Serializable
data class ValueRange(
    val values: List<List<String>>
)