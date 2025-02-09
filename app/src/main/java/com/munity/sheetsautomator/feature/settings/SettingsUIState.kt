package com.munity.sheetsautomator.feature.settings

data class SettingsUIState(
    val dialogSpreadsheetId: String = "",
    val isDialogTextFieldVisible: Boolean = false,
    val isSnackBarShowing: Boolean = false,
    val snackBarMessage: String = "",
)
