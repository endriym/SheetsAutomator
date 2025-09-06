package com.munity.sheetsautomator.core.data.repository

import com.munity.sheetsautomator.core.data.model.DataEntry
import com.munity.sheetsautomator.core.data.remote.model.ValueRange

fun DataEntry.asValueRange(): ValueRange =
    ValueRange(values = listOf(listOf(amount, date, category, description)))
