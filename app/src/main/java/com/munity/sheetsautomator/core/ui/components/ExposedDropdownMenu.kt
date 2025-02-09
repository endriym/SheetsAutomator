package com.munity.sheetsautomator.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

/*
    Refer to https://stackoverflow.com/questions/69939854/dropdownmenu-padding
    for further information.
    onGloballyPositioned is needed, otherwise DropDown menu exceeds for
    (still) unknown reasons the parent container with `fillMaxWidth` modifier.
    Waiting for ExposedDropDownMenu to solve this issue.
    https://stackoverflow.com/questions/67111020/exposed-drop-down-menu-for-jetpack-compose/67111599#67111599
 */
@Composable
fun ExposedDropdownMenu(
    textFieldValue: String,
    textFieldLabel: String,
    onTextFieldValueChange: (String) -> Unit = { },
    onDropDownMenuItemClick: (String) -> Unit,
    dropDownItems: List<String>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var columnWidth by remember { mutableStateOf(0) }

    Column(modifier.onGloballyPositioned { layoutCoordinates ->
        columnWidth = layoutCoordinates.size.width
    }) {
        TextField(
            label = { Text(text = textFieldLabel) },
            value = textFieldValue,
            onValueChange = onTextFieldValueChange,
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded },
                ) {
                    if (expanded) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowUp,
                            contentDescription = null,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                        )
                    }
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(
                with(LocalDensity.current) {
                    columnWidth.toDp()
                }
            ),
        ) {
            dropDownItems.forEach { dropDownItem ->
                DropdownMenuItem(
                    onClick = {
                        onDropDownMenuItemClick(dropDownItem)
                        expanded = false
                    },
                    text = {
                        Text(text = dropDownItem)
                    },
                )
            }
        }
    }
}
