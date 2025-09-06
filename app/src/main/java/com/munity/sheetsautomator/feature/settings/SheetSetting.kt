package com.munity.sheetsautomator.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.munity.sheetsautomator.R
import com.munity.sheetsautomator.core.ui.components.PreferenceComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetSetting(
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
    modifier: Modifier = Modifier,
) {
    PreferenceComponent(
        primaryText = "Sheet",
        summaryText = selectedSheet ?: "No sheet selected",
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Article,
                contentDescription = "Sheet title",
                modifier = Modifier.padding(24.dp)
            )
        },
        widget = null,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = true, onClick = onSheetSettingClick)
    )


    if (isSheetDialogVisible) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp

        Dialog(onDismissRequest = onSheetDialogDismissButtonClick) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.heightIn(max = (screenHeight * 0.75).dp)
            ) {
                Column {
                    Text(
                        text = "Select a spreadsheet from your Google Drive",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )

                    PullToRefreshBox(
                        isRefreshing = isSheetSettingRefreshing,
                        onRefresh = onSheetSettingRefresh,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .weight(1f, fill = false)
                    ) {
                        if (sheets.isNotEmpty()) {
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                items(sheets) { sheet ->
                                    SheetItem(
                                        sheet = sheet,
                                        onDialogSheetSelection = onDialogSheetSelection,
                                        isSelected = dialogSelectedSheet == sheet,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            ) {
                                Text(
                                    text = "No spreadsheets found. Please pull to refresh the page. " +
                                            "If none appear, make sure you have at least one " +
                                            "spreadsheet in your Google Drive.",
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        TextButton(onClick = onSheetDialogDismissButtonClick) {
                            Text(text = stringResource(R.string.dismiss))
                        }

                        TextButton(onClick = onSheetDialogConfirmButtonClick) {
                            Text(text = stringResource(R.string.confirm))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SheetItem(
    sheet: String,
    onDialogSheetSelection: (String) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = if (isSelected) MaterialTheme.colorScheme.surfaceBright else MaterialTheme.colorScheme.background)
            .clickable(
                enabled = true,
                onClick = {
                    onDialogSheetSelection(sheet)
                }
            )
    ) {
        Text(
            text = sheet,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(20.dp)
                .weight(1f)
        )

        if (isSelected)
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected spreadsheet",
                modifier = Modifier.padding(end = 16.dp)
            )
    }
}
