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

    val categoriesRange: StateFlow<String> = sheetsRepository.categoriesRange.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ""
    )

    fun refreshSheetTitles() {
        viewModelScope.launch {
            val result = sheetsRepository.refreshSheetTitles()

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
            newUiState.copy(isSpreadsheetIdDialogVisible = true)
        }
    }

    fun onSpreadsheetIdDialogValueChange(newSpreadsheetIdDialog: String) {
        _uiState.update { newUiState ->
            newUiState.copy(spreadsheetIdDialog = newSpreadsheetIdDialog)
        }
    }

    fun onSpreadsheetIdDialogConfirmButton() {
        viewModelScope.launch(Dispatchers.IO) {
            sheetsRepository.saveSpreadsheetId(_uiState.value.spreadsheetIdDialog)
        }

        _uiState.update { newUiState ->
            newUiState.copy(
                isSpreadsheetIdDialogVisible = false
            )
        }
    }

    fun onSpreadsheetIdDialogDismissButton() {
        _uiState.update { newUiState ->
            newUiState.copy(isSpreadsheetIdDialogVisible = false, spreadsheetIdDialog = "")
        }
    }

    fun onCategoriesRangeTrailingIconClick() {
        _uiState.update { newUiState ->
            newUiState.copy(isCategoriesRangeDialogTFVisible = true)
        }
    }

    fun onCategoriesRangeDialogValueChange(newCategoriesRangeDialog: String) {
        _uiState.update { newUiState ->
            newUiState.copy(categoriesRangeDialog = newCategoriesRangeDialog)
        }
    }

    fun onCategoriesRangeDialogConfirmButton() {
        viewModelScope.launch(Dispatchers.IO) {
            sheetsRepository.saveCategoriesRange(_uiState.value.categoriesRangeDialog)
        }

        _uiState.update { newUiState ->
            newUiState.copy(
                isCategoriesRangeDialogTFVisible = false
            )
        }
    }

    fun onCategoriesRangeDialogDismissButton() {
        _uiState.update { newUiState ->
            newUiState.copy(isCategoriesRangeDialogTFVisible = false, categoriesRangeDialog = "")
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