package com.munity.sheetsautomator.feature.settings

import com.munity.sheetsautomator.core.data.model.Spreadsheet

data class SettingsUIState(
    val isSpreadsheetDialogVisible: Boolean = false,
    val isSpreadsheetSettingRefreshing: Boolean = false,
    val spreadsheets: List<Spreadsheet> = emptyList(),
    val selectedDialogSpreadsheet: Spreadsheet? = null,

    val isSheetDialogVisible: Boolean = false,
    val isSheetSettingRefreshing: Boolean = false,
//    val sheets: List<String> = emptyList(),
    val selectedDialogSheet: String? = null,

    val dialogCategoriesRangeValue: String = "",
    val isCategoriesRangeDialogVisible: Boolean = false,
)
