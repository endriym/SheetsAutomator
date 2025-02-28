package com.munity.sheetsautomator.feature.settings.navigation

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
    onShowSnackbar: suspend (String) -> Boolean,
    modifier: Modifier = Modifier
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
        val categoriesRange: String by settingsViewModel.categoriesRange.collectAsState()

        SettingsScreen(
            spreadsheetIdTFValue = spreadsheetId,
            onSpreadsheetIdTFTrailingIconClick = settingsViewModel::onSpreadsheetIdTrailingIconClick,
            isSpreadsheetIdDialogTFVisible = uiState.isSpreadsheetIdDialogVisible,
            spreadsheetIdDialogTFValue = uiState.spreadsheetIdDialog,
            onDialogSpreadsheetIdValueChange = settingsViewModel::onSpreadsheetIdDialogValueChange,
            onSpreadsheetIdDialogDismissButtonClick = settingsViewModel::onSpreadsheetIdDialogDismissButton,
            onSpreadsheetIdDialogConfirmButtonClick = settingsViewModel::onSpreadsheetIdDialogConfirmButton,
            sheetTitleTFValue = sheetTitle,
            onDropDownSheetTitleClick = settingsViewModel::onDropDownSheetTitleClick,
            sheetTitles = sheetTitles,
            onSheetTitlesSync = settingsViewModel::refreshSheetTitles,
            onCategoriesSync = settingsViewModel::refreshCategories,
            categories = categories,
            isSnackBarShowing = uiState.isSnackBarShowing,
            snackBarMessage = uiState.snackBarMessage,
            onShowSnackbar = onShowSnackbar,
            onDismissSnackBar = settingsViewModel::onDismissSnackBar,
            modifier = modifier,
            categoriesRangeValue = categoriesRange,
            onCategoriesRangeTrailingIconClick = settingsViewModel::onCategoriesRangeTrailingIconClick,
            isCategoriesRangeDialogTFVisible = uiState.isCategoriesRangeDialogTFVisible,
            categoriesRangeDialogTFValue = uiState.categoriesRangeDialog,
            onCategoriesRangeDialogValueChange = settingsViewModel::onCategoriesRangeDialogValueChange,
            onCategoriesRangeDialogDismissButtonClick = settingsViewModel::onCategoriesRangeDialogDismissButton,
            onCategoriesRangeDialogConfirmButtonClick = settingsViewModel::onCategoriesRangeDialogConfirmButton,
        )
    }
}

