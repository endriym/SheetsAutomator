package com.munity.sheetsautomator.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PreferenceComponent(
    primaryText: String,
    summaryText: String,
    icon: (@Composable () -> Unit)? = null,
    widget: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f, fill = true)
        ) {
            icon?.invoke()

            Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {
                Text(text = primaryText, style = MaterialTheme.typography.labelLarge, fontSize = 22.sp)
                Text(text = summaryText, style = MaterialTheme.typography.bodyMedium)
            }
        }

        widget?.invoke()
    }
}

@Preview(showBackground = true)
@Composable
private fun PreferencePreview() {
    MaterialTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            PreferenceComponent(
                primaryText = "Sheet title",
                summaryText = "Sheet 1",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                widget = {
                    Switch(
                        checked = false,
                        onCheckedChange = {},
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            PreferenceComponent(
                primaryText = "Very very very very very very very long title",
                summaryText = "Very very very very very very very very very very very very long description",
                icon = {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = null,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                widget = {
                    Checkbox(
                        checked = false,
                        onCheckedChange = {},
                        modifier = Modifier.padding(end = 12.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
