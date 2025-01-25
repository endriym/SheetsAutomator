package com.munity.sheetsautomator.feature.home

import com.munity.sheetsautomator.util.DateUtil

data class HomeUIState(
    val amount: String = "",
    val date: String = DateUtil.getCurrentDayMonth(),
    val category: String = "",
    val description: String = "",
)
