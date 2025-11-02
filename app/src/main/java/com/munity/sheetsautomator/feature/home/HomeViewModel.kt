package com.munity.sheetsautomator.feature.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.munity.sheetsautomator.SheetsAutomatorApplication
import com.munity.sheetsautomator.core.data.model.DataEntry
import com.munity.sheetsautomator.core.data.repository.SheetsRepository
import com.munity.sheetsautomator.util.OAuthUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val sheetsRepository: SheetsRepository) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SheetsAutomatorApplication)
                HomeViewModel(application.sheetsRepository)
            }
        }
    }

    private val _uiState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    val snackbarMessages: SharedFlow<String?> = sheetsRepository.messages

    val isLoggedIn: StateFlow<Boolean> = sheetsRepository.isLoggedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    val categories: StateFlow<List<String>> = sheetsRepository.categories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = listOf(),
    )

    fun onAmountChange(newAmount: String) {
        _uiState.update { oldState ->
            oldState.copy(amount = newAmount)
        }
    }

    fun onDateChange(newDate: String) {
        _uiState.update { oldState ->
            oldState.copy(date = newDate)
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { oldState ->
            oldState.copy(description = newDescription)
        }
    }

    fun onDropDownMenuItemClick(categoryClicked: String) {
        _uiState.update { oldState ->
            oldState.copy(category = categoryClicked)
        }
    }

    fun onAddButtonClick() {
        viewModelScope.launch {
            val result = with(_uiState.value) {
                val message = if (amount.isNullOrBlank())
                    "Please enter a valid 'amount'"
                else if (date.isNullOrBlank())
                    "Please enter a valid 'date'"
                else if (category.isNullOrBlank())
                    "Please select a 'category'"
                else if (description.isNullOrBlank())
                    "Please enter a non blank description"
                else
                    null

                message?.let {
                    viewModelScope.launch {
                        sheetsRepository.emitMessage(message)
                    }

                    return@launch
                }

                sheetsRepository.appendRow(
                    range = "A:D",
                    dataEntry = DataEntry(
                        amount = amount!!,
                        date = date!!,
                        category = category!!,
                        description = description!!
                    )
                )
            }

            val message = if (result.isSuccess) {
                "Row was added successfully."
            } else {
                result.exceptionOrNull()?.message
                    ?: "Row was not added. Unknown problem!"
            }

            sheetsRepository.emitMessage(message)
        }
    }

    fun onSignInButtonClick(context: Context) {
        OAuthUtil.launchAuthentication(context)
    }
}
