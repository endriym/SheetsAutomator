package com.munity.sheetsautomator.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun SheetsBottomAppBar(
    onHomeButtonClick: () -> Unit,
    onSettingsButtonClick: () -> Unit,
) {
    BottomAppBar(
        actions = {
            IconButton(onClick = onHomeButtonClick) {
                Icon(imageVector = Icons.Filled.Home, contentDescription = "Home")
            }
            IconButton(onClick = onSettingsButtonClick) {
                Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    )
}