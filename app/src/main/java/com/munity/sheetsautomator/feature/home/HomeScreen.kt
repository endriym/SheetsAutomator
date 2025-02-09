package com.munity.sheetsautomator.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.munity.sheetsautomator.R
import com.munity.sheetsautomator.core.ui.components.DatePickerDocked
import com.munity.sheetsautomator.core.ui.components.ExposedDropdownMenu

@Composable
fun HomeScreen(
    isLoggedIn: Boolean,
    amount: String,
    onAmountChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    category: String,
    onDropDownMenuItemClick: (String) -> Unit,
    dropDownItems: List<String>,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    onSignInButtonClick: () -> Unit,
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

    if (isLoggedIn) {
        HomeScreenLoggedIn(
            amount = amount,
            onAmountChange = onAmountChange,
            onDateChange = onDateChange,
            category = category,
            onDropDownMenuItemClick = onDropDownMenuItemClick,
            dropDownItems = dropDownItems,
            description = description,
            onDescriptionChange = onDescriptionChange,
            onAddButtonClick = onAddButtonClick,
            modifier = modifier
        )
    } else {
        HomeScreenNotLoggedIn(onSignInButtonClick = onSignInButtonClick, modifier.fillMaxSize())
    }
}

@Composable
fun HomeScreenLoggedIn(
    amount: String,
    onAmountChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    category: String,
    onDropDownMenuItemClick: (String) -> Unit,
    dropDownItems: List<String>,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        TextField(
            value = amount,
            onValueChange = onAmountChange,
            label = {
                Text(text = stringResource(R.string.importo))
            },
            prefix = {
                Text(text = "€")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        DatePickerDocked(
            onDateChange = onDateChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        ExposedDropdownMenu(
            textFieldValue = category,
            textFieldLabel = stringResource(R.string.categoria),
            onDropDownMenuItemClick = onDropDownMenuItemClick,
            dropDownItems = dropDownItems,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        TextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = {
                Text(text = stringResource(R.string.descrizione))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Button(
            onClick = onAddButtonClick,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(stringResource(R.string.aggiungi))
        }
    }
}

@Composable
fun HomeScreenNotLoggedIn(onSignInButtonClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.accedi_info),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = onSignInButtonClick,
            colors = ButtonDefaults.buttonColors(),
        ) {
            Text(text = stringResource(R.string.login))
        }
    }
}