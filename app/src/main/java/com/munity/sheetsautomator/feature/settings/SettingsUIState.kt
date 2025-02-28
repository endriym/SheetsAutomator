package com.munity.sheetsautomator.feature.settings

data class SettingsUIState(
    val spreadsheetIdDialog: String = "",
    val isSpreadsheetIdDialogVisible: Boolean = false,
    val categoriesRangeDialog: String = "",
    val isCategoriesRangeDialogTFVisible: Boolean = false,
    val isSnackBarShowing: Boolean = false,
    val snackBarMessage: String = "",
)
