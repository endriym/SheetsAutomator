package com.munity.sheetsautomator.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.munity.sheetsautomator.R
import com.munity.sheetsautomator.core.ui.components.DialogTextField
import com.munity.sheetsautomator.core.ui.components.PreferenceComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesRangeSetting(
    categoriesRangeValue: String?,
    onCategoriesRangeSettingClick: () -> Unit,
    isCategoriesRangeDialogVisible: Boolean,
    categoriesRangeDialogTFValue: String,
    onCategoriesRangeDialogValueChange: (String) -> Unit,
    onCategoriesRangeDialogDismissButtonClick: () -> Unit,
    onCategoriesRangeDialogConfirmButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PreferenceComponent(
        primaryText = stringResource(R.string.categories_range),
        summaryText = categoriesRangeValue ?: "No categories range entered",
        icon = {
            Icon(
                imageVector = Icons.Default.AspectRatio,
                contentDescription = null,
                modifier = Modifier.padding(24.dp)
            )
        },
        widget = null,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = true, onClick = onCategoriesRangeSettingClick)
    )

    if (isCategoriesRangeDialogVisible) {
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
}
