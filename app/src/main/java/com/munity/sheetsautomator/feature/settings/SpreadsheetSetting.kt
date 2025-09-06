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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.munity.sheetsautomator.R
import com.munity.sheetsautomator.core.data.model.Spreadsheet
import com.munity.sheetsautomator.core.ui.components.PreferenceComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpreadsheetSetting(
    selectedSpreadsheet: Spreadsheet?,
    onSpreadsheetSettingClick: () -> Unit,
    isSpreadsheetDialogVisible: Boolean,
    onDialogSpreadsheetSelection: (Spreadsheet) -> Unit,
    isSpreadsheetSettingRefreshing: Boolean,
    onSpreadsheetSettingRefresh: () -> Unit,
    spreadsheets: List<Spreadsheet>,
    dialogSelectedSpreadSheet: Spreadsheet?,
    onSpreadsheetDialogDismissButtonClick: () -> Unit,
    onSpreadsheetDialogConfirmButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PreferenceComponent(
        primaryText = stringResource(R.string.spreadsheet_id),
        summaryText = selectedSpreadsheet?.name ?: "No spreadsheet selected",
        icon = {
            Icon(
                painter = painterResource(R.drawable.lists_icon_24px),
                contentDescription = null,
                modifier = Modifier.padding(24.dp)
            )
        },
        widget = null,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = true, onClick = onSpreadsheetSettingClick)
    )

    if (isSpreadsheetDialogVisible) {
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp

        Dialog(onDismissRequest = onSpreadsheetDialogDismissButtonClick) {
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
                        isRefreshing = isSpreadsheetSettingRefreshing,
                        onRefresh = onSpreadsheetSettingRefresh,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .weight(1f, fill = false)
                    ) {
                        if (spreadsheets.isNotEmpty()) {
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                items(spreadsheets) { spreadsheet ->
                                    SpreadsheetItem(
                                        spreadsheet = spreadsheet,
                                        onDialogSpreadsheetSelection = onDialogSpreadsheetSelection,
                                        isSelected = dialogSelectedSpreadSheet?.id == spreadsheet.id,
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
                        TextButton(onClick = onSpreadsheetDialogDismissButtonClick) {
                            Text(text = stringResource(R.string.dismiss))
                        }

                        TextButton(onClick = onSpreadsheetDialogConfirmButtonClick) {
                            Text(text = stringResource(R.string.confirm))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SpreadsheetItem(
    spreadsheet: Spreadsheet,
    onDialogSpreadsheetSelection: (Spreadsheet) -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 12.dp, top = 4.dp, end = 12.dp, bottom = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = if (isSelected) MaterialTheme.colorScheme.surfaceBright else MaterialTheme.colorScheme.background)
            .clickable(
                enabled = true,
                onClick = {
                    onDialogSpreadsheetSelection(spreadsheet)
                }
            )
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = spreadsheet.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)
            )

            Text(
                text = spreadsheet.id,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            )
        }

        if (isSelected)
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected spreadsheet",
                modifier = Modifier.padding(end = 16.dp)
            )
    }
}
