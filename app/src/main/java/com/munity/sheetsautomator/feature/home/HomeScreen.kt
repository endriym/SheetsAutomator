package com.munity.sheetsautomator.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    selectedDate: String,
    onDateChange: (String) -> Unit,
    category: String,
    onDropDownMenuItemClick: (String) -> Unit,
    dropDownItems: List<String>,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onAddButtonClick: () -> Unit,
    onSignInButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isLoggedIn) {
        HomeScreenLoggedIn(
            amount = amount,
            onAmountChange = onAmountChange,
            selectedDate = selectedDate,
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
    selectedDate: String,
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
        OutlinedTextField(
            value = amount,
            onValueChange = onAmountChange,
            label = {
                Text(text = stringResource(R.string.amount))
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
            selectedDate = selectedDate,
            onDateChange = onDateChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        ExposedDropdownMenu(
            textFieldValue = category,
            textFieldLabel = stringResource(R.string.category),
            onDropDownMenuItemClick = onDropDownMenuItemClick,
            dropDownItems = dropDownItems,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = {
                Text(text = stringResource(R.string.description))
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
            Text(stringResource(R.string.insert))
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
            text = stringResource(R.string.login_info),
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
