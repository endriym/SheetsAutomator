package com.munity.sheetsautomator.core.data.repository

sealed class SheetsException(override val message: String) : Exception(message) {
    class SpreadsheetIdNullException : Exception("Spreadsheet ID is null")
    class SheetTitleNullException : Exception("Sheet title is null")
}