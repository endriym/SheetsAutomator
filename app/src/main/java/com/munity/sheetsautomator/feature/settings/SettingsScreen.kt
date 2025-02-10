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
    spreadsheetIdValue: String,
    onSpreadsheetIdTrailingIconClick: () -> Unit,
    isDialogTextFieldVisible: Boolean,
    dialogSpreadsheetIdValue: String,
    onDialogSpreadsheetIdValueChange: (String) -> Unit,
    onDialogDismissButtonClick: () -> Unit,
    onDialogConfirmButtonClick: () -> Unit,
    sheetTitleValue: String,
    onDropDownSheetTitleClick: (String) -> Unit,
    sheetTitles: List<String>,
    onSheetTitlesSync: () -> Unit,
    categories: List<String>,
    onCategoriesSync: () -> Unit,
    categoriesRange: String,
    onCategoriesRangeChange: (String) -> Unit,
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
            value = spreadsheetIdValue,
            onValueChange = {},
            label = { Text(text = stringResource(R.string.spreadsheet_id)) },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = {
                    onSpreadsheetIdTrailingIconClick()
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

        if (isDialogTextFieldVisible) {
            DialogTextField(
                dialogTitle = stringResource(R.string.dialog_title),
                dialogDescription = stringResource(R.string.dialog_description),
                textFieldValue = dialogSpreadsheetIdValue,
                onTextFieldValueChange = onDialogSpreadsheetIdValueChange,
                textFieldLabel = stringResource(R.string.spreadsheet_id),
                onDialogDismissRequest = onDialogDismissButtonClick,
                onDismissButtonClick = onDialogDismissButtonClick,
                onConfirmButtonClick = onDialogConfirmButtonClick,
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
                textFieldValue = sheetTitleValue,
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

        TextField(
            value = categoriesRange,
            onValueChange = onCategoriesRangeChange,
            modifier = Modifier.padding(16.dp)
        )
    }
}