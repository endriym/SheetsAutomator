package com.munity.sheetsautomator.feature.home

import com.munity.sheetsautomator.core.data.model.DataEntry
import com.munity.sheetsautomator.util.DateUtil

data class HomeUIState(
    val dataEntry: DataEntry = DataEntry(
        amount = "",
        date = DateUtil.getCurrentDayMonth(),
        category = "",
        description = "",
    ),
)
