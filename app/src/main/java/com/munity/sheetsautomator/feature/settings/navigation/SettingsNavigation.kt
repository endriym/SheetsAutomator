package com.munity.sheetsautomator.feature.settings.navigation

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.munity.sheetsautomator.feature.settings.SettingsScreen
import com.munity.sheetsautomator.feature.settings.SettingsViewModel

fun NavController.navigateToSettings(navOptions: NavOptions? = null) =
    navigate("settings", navOptions)

fun NavGraphBuilder.settingsScreen(
    context: Context,
    onShowSnackbar: suspend (String) -> Boolean,
    modifier: Modifier
) {
    composable(route = "settings") { navBackStackEntry ->
        val settingsViewModel: SettingsViewModel = viewModel(
            factory = SettingsViewModel.Factory
        )

        val uiState by settingsViewModel.uiState.collectAsState()
        val spreadsheetId: String by settingsViewModel.spreadsheetId.collectAsState()
        val sheetTitle: String by settingsViewModel.sheetTitle.collectAsState()
        val categories: List<String> by settingsViewModel.categories.collectAsState()
        val sheetTitles: List<String> by settingsViewModel.sheetTitles.collectAsState()

        SettingsScreen(
            spreadsheetIdValue = spreadsheetId,
            onSpreadsheetIdTrailingIconClick = { settingsViewModel.onSpreadsheetIdTrailingIconClick() },
            isDialogTextFieldVisible = uiState.isDialogTextFieldVisible,
            dialogSpreadsheetIdValue = uiState.dialogSpreadsheetId,
            onDialogSpreadsheetIdValueChange = {
                settingsViewModel.onDialogSpreadsheetIdValueChange(it)
            },
            onDialogDismissButtonClick = { settingsViewModel.onDialogDismissButton() },
            onDialogConfirmButtonClick = { settingsViewModel.onDialogConfirmButton() },
            sheetTitleValue = sheetTitle,
            onDropDownSheetTitleClick = { settingsViewModel.onDropDownSheetTitleClick(it) },
            sheetTitles = sheetTitles,
            onSheetTitlesSync = settingsViewModel::refreshSheetTitles,
            onCategoriesSync = settingsViewModel::refreshCategories,
            categories = categories,
            isSnackBarShowing = uiState.isSnackBarShowing,
            snackBarMessage = uiState.snackBarMessage,
            onShowSnackbar = onShowSnackbar,
            onDismissSnackBar = settingsViewModel::onDismissSnackBar,
            modifier = modifier
        )
    }
}

