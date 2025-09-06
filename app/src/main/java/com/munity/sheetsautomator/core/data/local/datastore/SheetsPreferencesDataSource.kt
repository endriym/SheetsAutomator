package com.munity.sheetsautomator.core.data.local.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.munity.sheetsautomator.core.data.local.datastore.model.StoredPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SheetsPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) : PreferencesStorage {
    private companion object {
        /// The authorization code is needed for refresh and access tokens
        val AUTH_CODE_KEY = stringPreferencesKey("auth_code")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val EXPIRATION_KEY = stringPreferencesKey("expiration_date")
        val CATEGORIES_KEY = stringPreferencesKey("categories")
        val SPREADSHEET_ID_KEY = stringPreferencesKey("spreadsheet_id")
        val SPREADSHEET_NAME_KEY = stringPreferencesKey("spreadsheet_name")
        val SHEET_TITLE_KEY = stringPreferencesKey("sheet_title")
        val SHEET_TITLES_KEY = stringPreferencesKey("sheet_titles")
        val CATEGORIES_RANGE_KEY = stringPreferencesKey("categories_range")
        const val TAG = "SheetsPreferencesDataSource"
    }

    override val storedPreferences: Flow<StoredPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            StoredPreferences(
                authToken = preferences[AUTH_CODE_KEY],
                accessToken = preferences[ACCESS_TOKEN_KEY],
                expirationDate = preferences[EXPIRATION_KEY],
                refreshToken = preferences[REFRESH_TOKEN_KEY],
                spreadsheetId = preferences[SPREADSHEET_ID_KEY],
                spreadsheetName = preferences[SPREADSHEET_NAME_KEY],
                sheetTitle = preferences[SHEET_TITLE_KEY],
                sheetTitles = preferences[SHEET_TITLES_KEY]?.split(", "),
                categories = preferences[CATEGORIES_KEY]?.split(", "),
                categoriesRange = preferences[CATEGORIES_RANGE_KEY],
            )
        }

    override suspend fun saveAuthCode(authCode: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_CODE_KEY] = authCode
        }
    }

    override suspend fun saveAccessToken(accessToken: String, dateExp: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[EXPIRATION_KEY] = dateExp
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun saveCategories(categories: List<String>) {
        dataStore.edit { preferences ->
            preferences[CATEGORIES_KEY] = categories.joinToString()
        }
    }

    override suspend fun saveSpreadsheet(spreadsheetId: String, spreadsheetName: String) {
        dataStore.edit { preferences ->
            preferences[SPREADSHEET_ID_KEY] = spreadsheetId
            preferences[SPREADSHEET_NAME_KEY] = spreadsheetName
        }
    }

    override suspend fun saveSheetTitle(sheetTitle: String) {
        dataStore.edit { preferences ->
            preferences[SHEET_TITLE_KEY] = sheetTitle
        }
    }

    override suspend fun saveSheetTitles(sheetTitles: List<String>) {
        dataStore.edit { preferences ->
            preferences[SHEET_TITLES_KEY] = sheetTitles.joinToString()
        }
    }

    override suspend fun saveCategoriesRange(categoriesRange: String) {
        dataStore.edit { preferences ->
            preferences[CATEGORIES_RANGE_KEY] = categoriesRange
        }
    }
}
