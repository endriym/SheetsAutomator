package com.munity.sheetsautomator.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.munity.sheetsautomator.R
import com.munity.sheetsautomator.core.data.model.Spreadsheet
import com.munity.sheetsautomator.core.ui.components.PreferenceComponent

@Composable
fun SettingsScreen(
    // spreadsheet setting
    selectedSpreadsheet: Spreadsheet?,
    onSpreadsheetSettingClick: () -> Unit,
    isSpreadsheetDialogVisible: Boolean,
    onDialogSpreadsheetSelection: (Spreadsheet) -> Unit,
    isSpreadsheetSettingRefreshing: Boolean,
    onSpreadsheetSettingRefresh: () -> Unit,
    spreadsheets: List<Spreadsheet>,
    dialogSelectedSpreadsheet: Spreadsheet?,
    onSpreadsheetDialogDismissButtonClick: () -> Unit,
    onSpreadsheetDialogConfirmButtonClick: () -> Unit,

    // sheet setting
    selectedSheet: String?,
    onSheetSettingClick: () -> Unit,
    isSheetDialogVisible: Boolean,
    isSheetSettingRefreshing: Boolean,
    onSheetSettingRefresh: () -> Unit,
    sheets: List<String>,
    dialogSelectedSheet: String?,
    onDialogSheetSelection: (String) -> Unit,
    onSheetDialogDismissButtonClick: () -> Unit,
    onSheetDialogConfirmButtonClick: () -> Unit,

    // categories range setting
    categoriesRangeValue: String?,
    onCategoriesRangeSettingClick: () -> Unit,
    isCategoriesRangeDialogVisible: Boolean,
    categoriesRangeDialogTFValue: String,
    onCategoriesRangeDialogValueChange: (String) -> Unit,
    onCategoriesRangeDialogDismissButtonClick: () -> Unit,
    onCategoriesRangeDialogConfirmButtonClick: () -> Unit,

    //region Categories list fetched
    categories: List<String>,
    onCategoriesRefresh: () -> Unit,

    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier.verticalScroll(
            rememberScrollState()
        )
    ) {
        SpreadsheetSetting(
            selectedSpreadsheet = selectedSpreadsheet,
            onSpreadsheetSettingClick = onSpreadsheetSettingClick,
            isSpreadsheetDialogVisible = isSpreadsheetDialogVisible,
            onDialogSpreadsheetSelection = onDialogSpreadsheetSelection,
            isSpreadsheetSettingRefreshing = isSpreadsheetSettingRefreshing,
            onSpreadsheetSettingRefresh = onSpreadsheetSettingRefresh,
            spreadsheets = spreadsheets,
            dialogSelectedSpreadSheet = dialogSelectedSpreadsheet,
            onSpreadsheetDialogDismissButtonClick = onSpreadsheetDialogDismissButtonClick,
            onSpreadsheetDialogConfirmButtonClick = onSpreadsheetDialogConfirmButtonClick,
            modifier = Modifier.fillMaxWidth()
        )

        SheetSetting(
            selectedSheet = selectedSheet,
            onSheetSettingClick = onSheetSettingClick,
            isSheetDialogVisible = isSheetDialogVisible,
            isSheetSettingRefreshing = isSheetSettingRefreshing,
            onSheetSettingRefresh = onSheetSettingRefresh,
            sheets = sheets,
            dialogSelectedSheet = dialogSelectedSheet,
            onDialogSheetSelection = onDialogSheetSelection,
            onSheetDialogDismissButtonClick = onSheetDialogDismissButtonClick,
            onSheetDialogConfirmButtonClick = onSheetDialogConfirmButtonClick,
            modifier = Modifier.fillMaxWidth()
        )

        CategoriesRangeSetting(
            categoriesRangeValue = categoriesRangeValue,
            onCategoriesRangeSettingClick = onCategoriesRangeSettingClick,
            isCategoriesRangeDialogVisible = isCategoriesRangeDialogVisible,
            categoriesRangeDialogTFValue = categoriesRangeDialogTFValue,
            onCategoriesRangeDialogValueChange = onCategoriesRangeDialogValueChange,
            onCategoriesRangeDialogDismissButtonClick = onCategoriesRangeDialogDismissButtonClick,
            onCategoriesRangeDialogConfirmButtonClick = onCategoriesRangeDialogConfirmButtonClick,
            modifier = Modifier.fillMaxWidth()
        )

        PreferenceComponent(
            primaryText = stringResource(R.string.categories),
            summaryText = categories.joinToString(),
            icon = {
                Spacer(modifier = Modifier.padding(36.dp))
            },
            widget = null,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = true, onClick = onCategoriesRefresh)
        )
    }
}
