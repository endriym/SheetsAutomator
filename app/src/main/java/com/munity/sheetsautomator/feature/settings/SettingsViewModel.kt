package com.munity.sheetsautomator.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.munity.sheetsautomator.SheetsAutomatorApplication
import com.munity.sheetsautomator.core.data.model.Spreadsheet
import com.munity.sheetsautomator.core.data.repository.SheetsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val sheetsRepository: SheetsRepository,
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

    val snackbarMessages: SharedFlow<String?> = sheetsRepository.messages

    private var _uiState: MutableStateFlow<SettingsUIState> = MutableStateFlow(SettingsUIState())
    val uiState: StateFlow<SettingsUIState> = _uiState.asStateFlow()

    val selectedSpreadsheet: StateFlow<Spreadsheet?> = sheetsRepository.selectedSpreadsheet.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    val selectedSheet: StateFlow<String?> = sheetsRepository.sheetTitle.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    val sheets: StateFlow<List<String>> = sheetsRepository.sheetTitles.stateIn(
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

    /* ------ SPREADSHEET SETTING --------- */
    fun onSpreadsheetSettingClick() {
        _uiState.update { oldState ->
            oldState.copy(
                isSpreadsheetDialogVisible = true,
                selectedDialogSpreadsheet = selectedSpreadsheet.value
            )
        }
        refreshSpreadsheets()
    }

    fun onDialogSpreadsheetSelection(newDialogSpreadsheet: Spreadsheet) {
        _uiState.update { oldState ->
            oldState.copy(selectedDialogSpreadsheet = newDialogSpreadsheet)
        }
    }

    private fun refreshSpreadsheets() {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(isSpreadsheetSettingRefreshing = true)
            }

            sheetsRepository.getSpreadsheetFiles()
                .onSuccess { spreadsheets ->
                    _uiState.update { oldState ->
                        oldState.copy(spreadsheets = spreadsheets)
                    }
                }.onFailure { exception ->
                    sheetsRepository.emitMessage(exception.message)
                }

            _uiState.update { oldState ->
                oldState.copy(isSpreadsheetSettingRefreshing = false)
            }
        }
    }

    fun onSpreadsheetSettingRefresh() {
        refreshSpreadsheets()
    }

    fun onSpreadsheetDialogDismissButtonClick() {
        _uiState.update { oldState ->
            oldState.copy(isSpreadsheetDialogVisible = false)
        }
    }

    fun onSpreadsheetDialogConfirmButtonClick() {
        viewModelScope.launch {
            with(_uiState) {
                update { oldState ->
                    oldState.copy(isSpreadsheetDialogVisible = false)
                }

                //TODO handle null case
                if (value.selectedDialogSpreadsheet != null)
                    sheetsRepository.saveSpreadsheet(value.selectedDialogSpreadsheet!!)
            }
        }
    }

    /* ------ SHEET SETTING --------- */
    fun onSheetSettingClick() {
        _uiState.update { oldState ->
            oldState.copy(
                isSheetDialogVisible = true,
                selectedDialogSheet = selectedSheet.value
            )
        }
        refreshSheets()
    }

    fun onDialogSheetSelection(newDialogSheet: String) {
        _uiState.update { oldState ->
            oldState.copy(selectedDialogSheet = newDialogSheet)
        }
    }

    private fun refreshSheets() {
        viewModelScope.launch {
            _uiState.update { oldState ->
                oldState.copy(isSheetSettingRefreshing = true)
            }

            sheetsRepository.getSheetTitles()
                .onSuccess { sheets ->
//                    _uiState.update { oldState ->
//                        oldState.copy(sheets = sheets)
//                    }
                    sheetsRepository.emitMessage("Sheet titles successfully refreshed")
                }.onFailure { exception ->
                    sheetsRepository.emitMessage(exception.message)
                }

            _uiState.update { oldState ->
                oldState.copy(isSheetSettingRefreshing = false)
            }
        }
    }

    fun onSheetSettingRefresh() {
        refreshSheets()
    }

    fun onSheetDialogDismissButtonClick() {
        _uiState.update { oldState ->
            oldState.copy(isSheetDialogVisible = false)
        }
    }

    fun onSheetDialogConfirmButtonClick() {
        viewModelScope.launch {
            with(_uiState) {
                update { oldState ->
                    oldState.copy(isSheetDialogVisible = false)
                }

                //TODO handle null case
                if (value.selectedDialogSheet != null)
                    sheetsRepository.saveSheetTitle(value.selectedDialogSheet!!)
            }
        }
    }

    /* ------ CATEGORIES RANGE SETTING --------- */
    fun refreshCategories() {
        viewModelScope.launch {
            val result = sheetsRepository.refreshCategories()

            val message: String = if (result.isSuccess)
                "Sheet titles successfully refreshed."
            else
                result.exceptionOrNull()!!.message!!

            sheetsRepository.emitMessage(message)
        }
    }

    fun onCategoriesRangeSettingClick() {
        _uiState.update { newUiState ->
            newUiState.copy(
                isCategoriesRangeDialogVisible = true,
                dialogCategoriesRangeValue = categoriesRange.value,
            )
        }
    }

    fun onCategoriesRangeDialogValueChange(newCategoriesRangeDialog: String) {
        _uiState.update { newUiState ->
            newUiState.copy(dialogCategoriesRangeValue = newCategoriesRangeDialog)
        }
    }

    fun onCategoriesRangeDialogDismissButton() {
        _uiState.update { newUiState ->
            newUiState.copy(isCategoriesRangeDialogVisible = false)
        }
    }

    fun onCategoriesRangeDialogConfirmButton() {
        with(_uiState) {
            viewModelScope.launch(Dispatchers.IO) {
                sheetsRepository.saveCategoriesRange(value.dialogCategoriesRangeValue)
            }

            update { newUiState ->
                newUiState.copy(isCategoriesRangeDialogVisible = false)
            }
        }
    }
}
