package com.munity.sheetsautomator.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.munity.sheetsautomator.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetsTopAppBar(
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
//        .copy(containerColor = primaryContainerDark),
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontWeight = FontWeight.Bold) },
        colors = colors,
        modifier = Modifier.clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
    )
}