package com.munity.sheetsautomator.core.data.local.datastore

import com.munity.sheetsautomator.core.data.local.datastore.model.StoredPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesStorage {
    val storedPreferences: Flow<StoredPreferences>

    suspend fun saveAuthCode(authCode: String)
    suspend fun saveAccessToken(accessToken: String, dateExp: String)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun saveCategories(categories: List<String>)
    suspend fun saveSpreadsheetId(spreadsheetId: String)
    suspend fun saveSheetTitle(sheetTitle: String)
    suspend fun saveSheetTitles(sheetTitles: List<String>)
    suspend fun saveCategoriesRange(categoriesRange: String)
}
