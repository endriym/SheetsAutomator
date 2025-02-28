package com.munity.sheetsautomator.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.munity.sheetsautomator.R
import com.munity.sheetsautomator.core.ui.components.DialogTextField
import com.munity.sheetsautomator.core.ui.components.ExposedDropdownMenu

@Composable
fun SettingsScreen(
    //region Spreadsheet ID input
    spreadsheetIdTFValue: String,
    onSpreadsheetIdTFTrailingIconClick: () -> Unit,
    isSpreadsheetIdDialogTFVisible: Boolean,
    spreadsheetIdDialogTFValue: String,
    onDialogSpreadsheetIdValueChange: (String) -> Unit,
    onSpreadsheetIdDialogDismissButtonClick: () -> Unit,
    onSpreadsheetIdDialogConfirmButtonClick: () -> Unit,
    //endregion
    //region Sheet title input
    sheetTitleTFValue: String,
    onDropDownSheetTitleClick: (String) -> Unit,
    sheetTitles: List<String>,
    onSheetTitlesSync: () -> Unit,
    //endregion
    //region Categories list fetched
    categories: List<String>,
    onCategoriesSync: () -> Unit,
    //region Categories range input
    categoriesRangeValue: String,
    onCategoriesRangeTrailingIconClick: () -> Unit,
    isCategoriesRangeDialogTFVisible: Boolean,
    categoriesRangeDialogTFValue: String,
    onCategoriesRangeDialogValueChange: (String) -> Unit,
    onCategoriesRangeDialogDismissButtonClick: () -> Unit,
    onCategoriesRangeDialogConfirmButtonClick: () -> Unit,
    //endregion
    isSnackBarShowing: Boolean,
    snackBarMessage: String,
    onShowSnackbar: suspend (String) -> Boolean,
    onDismissSnackBar: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(isSnackBarShowing) {
        if (isSnackBarShowing) {
            val snackBarResult = onShowSnackbar(snackBarMessage)
            // On SnackBar dismissed (either by timeout or by user).
            if (snackBarResult) {
                onDismissSnackBar()
            }
        }
    }

    Column(verticalArrangement = Arrangement.Top, modifier = modifier) {
        TextField(
            value = spreadsheetIdTFValue,
            onValueChange = {},
            label = { Text(text = stringResource(R.string.spreadsheet_id)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    onSpreadsheetIdTFTrailingIconClick()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = "Modify spreadsheet ID",
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (isSpreadsheetIdDialogTFVisible) {
            DialogTextField(
                dialogTitle = stringResource(R.string.dialog_title),
                dialogDescription = stringResource(R.string.spreadsheet_id_dialog_description),
                textFieldValue = spreadsheetIdDialogTFValue,
                onTextFieldValueChange = onDialogSpreadsheetIdValueChange,
                textFieldLabel = stringResource(R.string.spreadsheet_id),
                onDialogDismissRequest = onSpreadsheetIdDialogDismissButtonClick,
                onDismissButtonClick = onSpreadsheetIdDialogDismissButtonClick,
                onConfirmButtonClick = onSpreadsheetIdDialogConfirmButtonClick,
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ExposedDropdownMenu(
                textFieldValue = sheetTitleTFValue,
                textFieldLabel = stringResource(R.string.sheet_title),
                onDropDownMenuItemClick = onDropDownSheetTitleClick,
                dropDownItems = sheetTitles,
                modifier = Modifier.weight(6f)
            )

            IconButton(onClick = onSheetTitlesSync, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh sheet titles"
                )
            }
        }

        TextField(
            value = categoriesRangeValue,
            onValueChange = {},
            label = { Text(text = stringResource(R.string.categories_range)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    onCategoriesRangeTrailingIconClick()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = stringResource(R.string.modify_categories_range),
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (isCategoriesRangeDialogTFVisible) {
            DialogTextField(
                dialogTitle = stringResource(R.string.modify_categories_range),
                dialogDescription = stringResource(R.string.categories_range_dialog_description),
                textFieldValue = categoriesRangeDialogTFValue,
                onTextFieldValueChange = onCategoriesRangeDialogValueChange,
                textFieldLabel = stringResource(R.string.categories_range),
                onDialogDismissRequest = onCategoriesRangeDialogDismissButtonClick,
                onDismissButtonClick = onCategoriesRangeDialogDismissButtonClick,
                onConfirmButtonClick = onCategoriesRangeDialogConfirmButtonClick,
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                value = categories.joinToString(),
                onValueChange = {},
                label = { Text(text = stringResource(R.string.categories)) },
                readOnly = true,
                modifier = Modifier.weight(6f)
            )

            IconButton(
                onClick = onCategoriesSync,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh categories"
                )
            }
        }
    }
}