package com.munity.sheetsautomator.feature.settings.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.munity.sheetsautomator.core.data.model.Spreadsheet
import com.munity.sheetsautomator.feature.settings.SettingsScreen
import com.munity.sheetsautomator.feature.settings.SettingsViewModel
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute

fun NavController.navigateToSettings(navOptions: NavOptions? = null) =
    navigate(SettingsRoute, navOptions)

fun NavGraphBuilder.settingsScreen(
    showSnackbar: suspend (String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    composable<SettingsRoute> { navBackStackEntry ->
        val settingsVM: SettingsViewModel = viewModel(
            factory = SettingsViewModel.Factory
        )

        val settingsUiState by settingsVM.uiState.collectAsState()
        val selectedSpreadsheet: Spreadsheet? by settingsVM.selectedSpreadsheet.collectAsState()
        val selectedSheet: String? by settingsVM.selectedSheet.collectAsState()
        val sheets: List<String> by settingsVM.sheets.collectAsState()
        val categories: List<String> by settingsVM.categories.collectAsState()
        val categoriesRange: String by settingsVM.categoriesRange.collectAsState()

        LaunchedEffect(key1 = true) {
            settingsVM.snackbarMessages.collect { message ->
                message?.let {
                    showSnackbar(it)
                }
            }
        }

        SettingsScreen(
            selectedSpreadsheet = selectedSpreadsheet,
            onSpreadsheetSettingClick = settingsVM::onSpreadsheetSettingClick,
            isSpreadsheetDialogVisible = settingsUiState.isSpreadsheetDialogVisible,
            onDialogSpreadsheetSelection = settingsVM::onDialogSpreadsheetSelection,
            isSpreadsheetSettingRefreshing = settingsUiState.isSpreadsheetSettingRefreshing,
            onSpreadsheetSettingRefresh = settingsVM::onSpreadsheetSettingRefresh,
            spreadsheets = settingsUiState.spreadsheets,
            dialogSelectedSpreadsheet = settingsUiState.selectedDialogSpreadsheet,
            onSpreadsheetDialogDismissButtonClick = settingsVM::onSpreadsheetDialogDismissButtonClick,
            onSpreadsheetDialogConfirmButtonClick = settingsVM::onSpreadsheetDialogConfirmButtonClick,


            selectedSheet = selectedSheet,
            onSheetSettingClick = settingsVM::onSheetSettingClick,
            isSheetDialogVisible = settingsUiState.isSheetDialogVisible,
            isSheetSettingRefreshing = settingsUiState.isSheetSettingRefreshing,
            onSheetSettingRefresh = settingsVM::onSheetSettingRefresh,
            sheets = sheets,
            dialogSelectedSheet = settingsUiState.selectedDialogSheet,
            onDialogSheetSelection = settingsVM::onDialogSheetSelection,
            onSheetDialogDismissButtonClick = settingsVM::onSheetDialogDismissButtonClick,
            onSheetDialogConfirmButtonClick = settingsVM::onSheetDialogConfirmButtonClick,

            categoriesRangeValue = categoriesRange,
            onCategoriesRangeSettingClick = settingsVM::onCategoriesRangeSettingClick,
            isCategoriesRangeDialogVisible = settingsUiState.isCategoriesRangeDialogVisible,
            categoriesRangeDialogTFValue = settingsUiState.dialogCategoriesRangeValue,
            onCategoriesRangeDialogValueChange = settingsVM::onCategoriesRangeDialogValueChange,
            onCategoriesRangeDialogDismissButtonClick = settingsVM::onCategoriesRangeDialogDismissButton,
            onCategoriesRangeDialogConfirmButtonClick = settingsVM::onCategoriesRangeDialogConfirmButton,

            onCategoriesRefresh = settingsVM::refreshCategories,
            categories = categories,

            modifier = modifier,
        )
    }
}

