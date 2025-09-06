package com.munity.sheetsautomator.core.data.repository

import com.munity.sheetsautomator.core.data.model.Spreadsheet
import com.munity.sheetsautomator.core.data.remote.model.DriveFileResponse

fun DriveFileResponse.asExternalModel(): Spreadsheet = Spreadsheet(
    id = id,
    name = name
)
