package com.munity.sheetsautomator.core.data.remote.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class DriveGetFilesResponse(
    val files: List<DriveFileResponse>,
)
