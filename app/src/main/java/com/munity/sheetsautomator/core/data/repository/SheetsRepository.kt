package com.munity.sheetsautomator.core.data.repository

import com.munity.sheetsautomator.core.data.local.database.SheetsAutomatorDatabase
import com.munity.sheetsautomator.core.data.local.datastore.PreferencesStorage
import com.munity.sheetsautomator.core.data.model.DataEntry
import com.munity.sheetsautomator.core.data.model.Spreadsheet
import com.munity.sheetsautomator.core.data.remote.SheetsApi
import com.munity.sheetsautomator.util.DateUtil
import com.munity.sheetsautomator.util.OAuthUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SheetsRepository(
    private val prefsStorage: PreferencesStorage,
    private val sheetsDatabase: SheetsAutomatorDatabase,
    private val sheetsApi: SheetsApi,
) {
    companion object {
        private const val DEFAULT_APPEND_ROW_RANGE = "A1:D1"
    }

    val isLoggedIn: Flow<Boolean> = prefsStorage.storedPreferences.map { storedPreferences ->
        storedPreferences.refreshToken?.isNotEmpty() ?: false
    }

    val categories: Flow<List<String>> = prefsStorage.storedPreferences.map { storedPrefs ->
        storedPrefs.categories ?: emptyList()
    }

    val selectedSpreadsheet: Flow<Spreadsheet?> =
        prefsStorage.storedPreferences.map { storedPrefs ->
            if (storedPrefs.spreadsheetId != null && storedPrefs.spreadsheetName != null)
                Spreadsheet(id = storedPrefs.spreadsheetId, name = storedPrefs.spreadsheetName)
            else
                null
        }

    val sheetTitle: Flow<String?> = prefsStorage.storedPreferences.map { storedPrefs ->
        storedPrefs.sheetTitle
    }

    val sheetTitles: Flow<List<String>> = prefsStorage.storedPreferences.map { storedPrefs ->
        storedPrefs.sheetTitles ?: emptyList()
    }

    val categoriesRange: Flow<String> = prefsStorage.storedPreferences.map { storedPrefs ->
        storedPrefs.categoriesRange ?: ""
    }

    private val _messages = MutableSharedFlow<String?>()
    val messages: SharedFlow<String?> = _messages.asSharedFlow()

    suspend fun emitMessage(message: String?) = _messages.emit(message)

    /**
     * Exchange authorization code for the first refresh and access tokens.
     * It saves it, then, in the DataStore.
     */
    private suspend fun getFirstAccessRefreshTokens(authorizationCode: String): String {
        val result = sheetsApi.getAccessRefreshTokens(authorizationCode, isRefresh = false)

        if (result.isSuccess) {
            val tokenInfo = result.getOrNull()!!

            prefsStorage.apply {
                saveAccessToken(
                    tokenInfo.accessToken,
                    DateUtil.getIso8601FromNowIn(tokenInfo.expiresIn)
                )
                tokenInfo.refreshToken?.let { refreshToken ->
                    saveRefreshToken(refreshToken)
                }
            }

            return tokenInfo.accessToken
        }

        throw result.exceptionOrNull()!!
    }

    /**
     * Extracts the authentication code from onResume()'s intent's data through a regex.
     * Finally, it saves the auth code in the DataStore, checking if the requested scope was granted.
     * Important: this code will be valid for a short period, so it's recommended to exchange it
     * for access and refresh tokens.
     */
    suspend fun exchangeAuthCode(intentDataStr: String): String? {
        val pattern = ".*code=(?<code>.+)&scope=(?<scope>.*)&?"
        val matchResult = pattern.toRegex().matchEntire(intentDataStr)

        matchResult?.let {
            val authCode = it.groups["code"]?.value
            val permittedScopes = it.groups["scope"]?.value

            if (
                permittedScopes!!.contains(OAuthUtil.GOOGLE_DRIVE_SCOPE)
                && permittedScopes.contains(OAuthUtil.GOOGLE_SHEETS_SCOPE)
                && authCode != null
            ) {
                prefsStorage.saveAuthCode(authCode)
                // Immediately exchange auth code for access and refresh tokens.
                getFirstAccessRefreshTokens(authCode)
                return authCode
            }
        }

        return null
    }

    suspend fun appendRow(
        range: String = DEFAULT_APPEND_ROW_RANGE,
        dataEntry: DataEntry,
    ): Result<Unit> {
        val storedPrefs = prefsStorage.storedPreferences.first()
        val valueRange = dataEntry.asValueRange()

        return sheetsApi.appendRows(
            spreadsheetID = storedPrefs.spreadsheetId!!,
            range = "${storedPrefs.sheetTitle}!$range",
            valueRange = valueRange,
        )
    }

    suspend fun refreshCategories(): Result<Unit> {
        val storedPrefs = prefsStorage.storedPreferences.first()

        if (storedPrefs.spreadsheetId == null)
            return Result.failure(SheetsException.SpreadsheetIdNullException())

        if (storedPrefs.sheetTitle == null)
            return Result.failure(SheetsException.SheetTitleNullException())

        if (storedPrefs.categoriesRange == null)
            return Result.failure(SheetsException.CategoriesRangeNullException())

        val result = sheetsApi.getCategories(
            spreadsheetID = storedPrefs.spreadsheetId,
            sheetTitle = storedPrefs.sheetTitle,
            range = storedPrefs.categoriesRange,
        )

        if (result.isSuccess) {
            prefsStorage.saveCategories(result.getOrNull()!!)
            return Result.success(Unit)
        }

        return Result.failure(result.exceptionOrNull()!!)
    }

    suspend fun getSpreadsheetFiles(): Result<List<Spreadsheet>> =
        sheetsApi.getSpreadsheetFiles().map { result -> result.map { it.asExternalModel() } }

    suspend fun getSheetTitles(): Result<Unit> {
        val selectedSpreadsheet = selectedSpreadsheet.first()

        selectedSpreadsheet?.id?.let { spreadsheetId ->
            val result = sheetsApi.getSheetTitles(spreadsheetId)

            result.onSuccess { sheetTitles ->
                prefsStorage.saveSheetTitles(sheetTitles)
                return Result.success(Unit)
            }

            return Result.failure(result.exceptionOrNull()!!)
        }

        return Result.failure(SheetsException.SpreadsheetIdNullException())
    }

    suspend fun saveSheetTitle(sheetTitle: String) {
        prefsStorage.saveSheetTitle(sheetTitle)
    }

    suspend fun saveSpreadsheet(newSpreadsheet: Spreadsheet) {
        prefsStorage.saveSpreadsheet(newSpreadsheet.id, newSpreadsheet.name)
    }

    suspend fun saveCategoriesRange(newCategoriesRange: String) {
        prefsStorage.saveCategoriesRange(newCategoriesRange)
    }
}
