package com.munity.sheetsautomator.core.data.model

data class StoredPreferences(
    val authToken: String?,
    val accessToken: String?,
    val refreshToken: String?,
    val expiresIn: String?,
    val spreadsheetId: String?,
    val sheetTitle: String?,
    val sheetTitles: List<String>?,
    val categories: List<String>?,
    val categoriesRange: String?,
)