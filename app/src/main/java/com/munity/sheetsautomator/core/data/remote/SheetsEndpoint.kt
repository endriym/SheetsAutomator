package com.munity.sheetsautomator.core.data.remote

object SheetsEndpoint {
    const val GOOGLE_ACCESS_REFRESH_ENDPOINT =
        "https://accounts.google.com/o/oauth2/token"
    const val GOOGLE_DRIVE_SPREADSHEETS_ENDPOINT =
        "https://www.googleapis.com/drive/v3/files?q=mimeType='application/vnd.google-apps.spreadsheet'"
    const val GOOGLE_SPREADSHEET_ENDPOINT =
        "https://sheets.googleapis.com/v4/spreadsheets/%s"
    const val GOOGLE_SHEETS_VALUES_ENDPOINT =
        "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s"
    const val GOOGLE_SHEETS_APPEND_ENDPOINT =
        "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s:append?valueInputOption=USER_ENTERED&insertDataOption=OVERWRITE"
}
