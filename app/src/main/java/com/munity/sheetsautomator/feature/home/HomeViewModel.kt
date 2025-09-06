package com.munity.sheetsautomator.feature.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.munity.sheetsautomator.SheetsAutomatorApplication
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
            oldState.copy(dataEntry = oldState.dataEntry.copy(amount = newAmount))
        }
    }

    fun onDateChange(newDate: String) {
        _uiState.update { oldState ->
            oldState.copy(dataEntry = oldState.dataEntry.copy(date = newDate))
        }
    }

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { oldState ->
            oldState.copy(dataEntry = oldState.dataEntry.copy(description = newDescription))
        }
    }

    fun onDropDownMenuItemClick(categoryClicked: String) {
        _uiState.update { oldState ->
            oldState.copy(dataEntry = oldState.dataEntry.copy(category = categoryClicked))
        }
    }

    fun onAddButtonClick() {
        viewModelScope.launch {
            val result =
                sheetsRepository.appendRow(range = "A:D", dataEntry = _uiState.value.dataEntry)

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
