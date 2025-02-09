package com.munity.sheetsautomator.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.munity.sheetsautomator.SheetsAutomatorApplication
import com.munity.sheetsautomator.core.data.repository.SheetsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val sheetsRepository: SheetsRepository
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as SheetsAutomatorApplication)
                SettingsViewModel(
                    application.sheetsRepository,
                )
            }
        }
    }

    private var _uiState: MutableStateFlow<SettingsUIState> = MutableStateFlow(SettingsUIState())
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()

    val spreadsheetId: StateFlow<String> = sheetsRepository.spreadsheetId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ""
    )

    val sheetTitle: StateFlow<String> = sheetsRepository.sheetTitle.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ""
    )

    val sheetTitles: StateFlow<List<String>> = sheetsRepository.sheetTitles.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    val categories: StateFlow<List<String>> = sheetsRepository.categories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun refreshSheetTitles() {
        viewModelScope.launch {
            val result = sheetsRepository.refreshSheetTitles(true)

            val message: String = if (result.isSuccess)
                "Sheet titles successfully refreshed."
            else
                result.exceptionOrNull()!!.message!!

            _uiState.update { newUiState ->
                newUiState.copy(
                    isSnackBarShowing = true,
                    snackBarMessage = message
                )
            }
        }
    }

    fun refreshCategories() {
        viewModelScope.launch {
            val result = sheetsRepository.refreshCategories()

            val message: String = if (result.isSuccess)
                "Sheet titles successfully refreshed."
            else
                result.exceptionOrNull()!!.message!!

            _uiState.update { newUiState ->
                newUiState.copy(
                    isSnackBarShowing = true,
                    snackBarMessage = message
                )
            }
        }
    }

    fun onSpreadsheetIdTrailingIconClick() {
        _uiState.update { newUiState ->
            newUiState.copy(isDialogTextFieldVisible = true)
        }
    }

    fun onDialogSpreadsheetIdValueChange(newDialogSpreadsheetId: String) {
        _uiState.update { newUiState ->
            newUiState.copy(dialogSpreadsheetId = newDialogSpreadsheetId)
        }
    }

    fun onDialogConfirmButton() {
        viewModelScope.launch(Dispatchers.IO) {
            sheetsRepository.saveSpreadsheetId(_uiState.value.dialogSpreadsheetId)
        }

        _uiState.update { newUiState ->
            newUiState.copy(
                isDialogTextFieldVisible = false
            )
        }
    }

    fun onDialogDismissButton() {
        _uiState.update { newUiState ->
            newUiState.copy(isDialogTextFieldVisible = false, dialogSpreadsheetId = "")
        }
    }

    fun onDropDownSheetTitleClick(newSheetTitle: String) {
        viewModelScope.launch(Dispatchers.IO) { sheetsRepository.saveSheetTitle(newSheetTitle) }
    }

    fun onDismissSnackBar() {
        _uiState.update { newUiState ->
            newUiState.copy(isSnackBarShowing = false, snackBarMessage = "")
        }
    }
}