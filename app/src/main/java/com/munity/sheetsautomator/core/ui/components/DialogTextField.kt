package com.munity.sheetsautomator.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.munity.sheetsautomator.R

@Composable
fun DialogTextField(
    dialogTitle: String,
    dialogDescription: String,
    textFieldValue: String,
    onTextFieldValueChange: (String) -> Unit,
    textFieldLabel: String,
    onDialogDismissRequest: () -> Unit,
    onDismissButtonClick: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    Dialog(onDismissRequest = onDialogDismissRequest) {
        Card(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = dialogTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Text(
                    text = dialogDescription,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                TextField(
                    value = textFieldValue,
                    label = { Text(text = textFieldLabel) },
                    onValueChange = onTextFieldValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismissButtonClick) {
                        Text(text = stringResource(R.string.dismiss))
                    }

                    TextButton(onClick = onConfirmButtonClick) {
                        Text(text = stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}
