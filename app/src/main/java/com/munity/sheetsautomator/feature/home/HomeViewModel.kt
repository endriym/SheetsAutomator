package com.munity.sheetsautomator.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.munity.sheetsautomator.SheetsAutomatorApplication
import com.munity.sheetsautomator.core.data.local.datastore.SheetsPreferencesDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val sheetsPreferencesDataSource: SheetsPreferencesDataSource
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SheetsAutomatorApplication)
                HomeViewModel(application.sheetsPreferencesDataSource)
            }
        }
    }

    private val _uiState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> =
        sheetsPreferencesDataSource.accessToken.map { accessToken ->
            accessToken.isNotEmpty()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun onAmountChange(newAmount: String): Unit {
        _uiState.update { oldState ->
            oldState.copy(amount = newAmount)
        }
    }

    fun onDateChange(newDate: String): Unit {
        _uiState.update { oldState ->
            oldState.copy(date = newDate)
        }
    }

    fun onDescriptionChange(newDescription: String): Unit {
        _uiState.update { oldState ->
            oldState.copy(description = newDescription)
        }
    }

    fun onDropDownMenuItemClick(categoryClicked: String): Unit {
        _uiState.update { oldState ->
            oldState.copy(category = categoryClicked)
        }
    }

    fun onAddButtonClick(): Unit {

    }

    fun onSignInButtonClick(): Unit {

    }
}
