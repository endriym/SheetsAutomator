package com.munity.sheetsautomator.core.data.local.datastore.model

data class StoredPreferences(
    val authToken: String?,
    val accessToken: String?,
    val refreshToken: String?,
    val expirationDate: String?,
    val spreadsheetId: String?,
    val spreadsheetName: String?,
    val sheetTitle: String?,
    val sheetTitles: List<String>?,
    val categories: List<String>?,
    val categoriesRange: String?,
)
