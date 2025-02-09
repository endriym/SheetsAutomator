package com.munity.sheetsautomator.core.data.repository

import com.munity.sheetsautomator.core.data.local.datastore.SheetsPreferencesDataSource
import com.munity.sheetsautomator.core.data.model.StoredPreferences
import com.munity.sheetsautomator.core.data.model.gsheet.ValueRange
import com.munity.sheetsautomator.core.data.remote.SheetsAPIDataSource
import com.munity.sheetsautomator.util.DateUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SheetsRepository(
    private val sheetsPrefsDS: SheetsPreferencesDataSource,
    private val sheetsAPIDataSource: SheetsAPIDataSource,
) {
    companion object {
        private const val TAG = "SheetsRepository"
        private const val SCOPE = "https://www.googleapis.com/auth/spreadsheets"
        private const val DEFAULT_CATEGORIES_RANGE = "F2:F"
        private const val DEFAULT_APPEND_ROW_RANGE = "A:D"
    }

    val isLoggedIn: Flow<Boolean> = sheetsPrefsDS.storedPreferences.map { storedPreferences ->
        storedPreferences.refreshToken?.isNotEmpty() ?: false
    }

    val categories: Flow<List<String>> = sheetsPrefsDS.storedPreferences.map { storedPrefs ->
        storedPrefs.categories ?: emptyList()
    }

    val spreadsheetId: Flow<String> = sheetsPrefsDS.storedPreferences.map { storedPrefs ->
        storedPrefs.spreadsheetId ?: ""
    }

    val sheetTitle: Flow<String> = sheetsPrefsDS.storedPreferences.map { storedPrefs ->
        storedPrefs.sheetTitle ?: ""
    }

    val sheetTitles: Flow<List<String>> = sheetsPrefsDS.storedPreferences.map { storedPrefs ->
        storedPrefs.sheetTitles ?: emptyList()
    }


    /**
     * Exchange authorization code for refresh and access tokens.
     * It saves it, then, in the DataStore.
     */
    private suspend fun updateAccessRefreshTokens(
        authorizationCode: String,
        isRefresh: Boolean
    ): String {
        val result = sheetsAPIDataSource.getAccessRefreshTokens(authorizationCode, isRefresh)

        if (result.isSuccess) {
            val tokenInfo = result.getOrNull()!!

            sheetsPrefsDS.apply {
                saveAccessToken(tokenInfo.accessToken, DateUtil.getDateIn(tokenInfo.expiresIn))
                tokenInfo.refreshToken?.let { refreshToken ->
                    saveRefreshToken(refreshToken)
                }
            }

            return tokenInfo.accessToken
        }

        throw result.exceptionOrNull()!!
    }

    private suspend fun getAccessToken(storedPrefs: StoredPreferences): String = when {
        storedPrefs.expiresIn != null && !DateUtil.hasExpired(storedPrefs.expiresIn) -> {
            storedPrefs.accessToken!!
        }

        storedPrefs.expiresIn != null && DateUtil.hasExpired(storedPrefs.expiresIn) -> {
            updateAccessRefreshTokens(storedPrefs.refreshToken!!, isRefresh = true)
        }

        else -> {
            throw IllegalStateException("Login needs to be redone!")
        }
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

            if (permittedScopes == SCOPE && authCode != null) {
                sheetsPrefsDS.saveAuthCode(authCode)
                // Immediately exchange auth code for access and refresh tokens.
                updateAccessRefreshTokens(authCode, isRefresh = false)
                return authCode
            }
        }

        return null
    }

    suspend fun appendRow(
        range: String = DEFAULT_APPEND_ROW_RANGE,
        valueRange: ValueRange
    ): Result<Unit> {
        val storedPrefs = sheetsPrefsDS.storedPreferences.first()
        val accessToken = getAccessToken(storedPrefs)

        return sheetsAPIDataSource.appendRows(
            spreadsheetID = storedPrefs.spreadsheetId!!,
            accessToken = accessToken,
            range = range,
            valueRange = valueRange,
        )
    }

    suspend fun refreshCategories(range: String = DEFAULT_CATEGORIES_RANGE): Result<Unit> {
        val storedPrefs = sheetsPrefsDS.storedPreferences.first()
        val accessToken = getAccessToken(storedPrefs)

        if (storedPrefs.spreadsheetId == null) {
            return Result.failure(SheetsException.SpreadsheetIdNullException())
        }

        if (storedPrefs.sheetTitle == null)
            return Result.failure(SheetsException.SheetTitleNullException())

        val result = sheetsAPIDataSource.getCategories(
            spreadsheetID = storedPrefs.spreadsheetId,
            sheetTitle = storedPrefs.sheetTitle,
            accessToken = accessToken,
            range = range,
        )

        if (result.isSuccess) {
            sheetsPrefsDS.saveCategories(result.getOrNull()!!)
            return Result.success(Unit)
        }

        return Result.failure(result.exceptionOrNull()!!)
    }

    suspend fun refreshSheetTitles(isRefresh: Boolean): Result<Unit> {
        val storedPrefs = sheetsPrefsDS.storedPreferences.first()
        val accessToken = getAccessToken(storedPrefs)

        storedPrefs.spreadsheetId?.let {
            val result = sheetsAPIDataSource.getSheetTitles(storedPrefs.spreadsheetId, accessToken)

            if (result.isSuccess) {
                sheetsPrefsDS.saveSheetTitles(result.getOrNull()!!)
                return Result.success(Unit)
            }

            return Result.failure(result.exceptionOrNull()!!)
        }

        return Result.failure(SheetsException.SpreadsheetIdNullException())
    }

    suspend fun saveSheetTitle(sheetTitle: String) {
        sheetsPrefsDS.saveSheetTitle(sheetTitle)
    }

    suspend fun saveSpreadsheetId(newSpreadsheetId: String) {
        sheetsPrefsDS.saveSpreadsheetId(newSpreadsheetId)
    }
}