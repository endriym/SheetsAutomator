package com.munity.sheetsautomator.core.data.remote

import com.munity.sheetsautomator.BuildConfig
import com.munity.sheetsautomator.core.data.model.gsheet.ErrorInfo
import com.munity.sheetsautomator.core.data.model.gsheet.Spreadsheet
import com.munity.sheetsautomator.core.data.model.gsheet.TokenInfo
import com.munity.sheetsautomator.core.data.model.gsheet.ValueRange
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class SheetsAPIDataSource {
    companion object {
        private const val CLIENT_ID = BuildConfig.CLIENT_ID
        private const val SCOPE = "https://www.googleapis.com/auth/spreadsheets"
        private const val GOOGLE_ACCESS_REFRESH_ENDPOINT =
            "https://accounts.google.com/o/oauth2/token"
        private const val GOOGLE_SPREADSHEET_ENDPOINT =
            "https://sheets.googleapis.com/v4/spreadsheets/%s"
        private const val GOOGLE_SHEETS_VALUES_ENDPOINT =
            "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s?access_token=%s"
        private const val GOOGLE_SHEETS_APPEND_ENDPOINT =
            "https://sheets.googleapis.com/v4/spreadsheets/%s/values/%s:append?valueInputOption=USER_ENTERED&insertDataOption=INSERT_ROWS"
        private const val PACKAGE_NAME = "com.munity.sheetsautomator"
        private const val TAG = "SheetsDataSource"
    }

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getCategories(
        spreadsheetID: String,
        sheetTitle: String,
        accessToken: String,
        range: String
    ): Result<List<String>> {
        val response = httpClient.get(
            urlString = GOOGLE_SHEETS_VALUES_ENDPOINT.format(
                spreadsheetID,
                "$sheetTitle!$range",
                accessToken
            )
        )

        return checkReturnResult(response) { responseToTransform ->
            responseToTransform.body<ValueRange>().values.flatten()
        }
    }

    suspend fun appendRows(
        spreadsheetID: String,
        accessToken: String,
        range: String,
        valueRange: ValueRange
    ): Result<Unit> {
        val response =
            httpClient.post(
                urlString = GOOGLE_SHEETS_APPEND_ENDPOINT.format(spreadsheetID, range),
            ) {
                header(HttpHeaders.Authorization, "Bearer $accessToken")
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(valueRange))
            }

        return checkReturnResult(response) {}
    }

    suspend fun getAccessRefreshTokens(
        authorizationCode: String,
        isRefresh: Boolean
    ): Result<TokenInfo> {
        val response: HttpResponse = httpClient.submitForm(
            url = GOOGLE_ACCESS_REFRESH_ENDPOINT,
            formParameters = parameters {
                append("grant_type", if (isRefresh) "refresh_token" else "authorization_code")
                if (isRefresh) append("refresh_token", authorizationCode)
                if (!isRefresh) append("code", authorizationCode)
                append("client_id", CLIENT_ID)
                append("redirect_uri", "${PACKAGE_NAME}:")
            }
        ).body()
        val responseAsText = response.bodyAsText()
        var errorInfoString: String? = null

        if (responseAsText.contains("error")) {
            val errorInfo: ErrorInfo = Json.decodeFromString(responseAsText)
            errorInfoString = errorInfo.toString()
        }

        return checkReturnResult(response, errorInfoString) { responseToTransform ->
            Json.decodeFromString<TokenInfo>(responseToTransform.bodyAsText())
        }
    }

    suspend fun getSheetTitles(spreadsheetID: String, accessToken: String): Result<List<String>> {
        val response =
            httpClient.get(urlString = GOOGLE_SPREADSHEET_ENDPOINT.format(spreadsheetID)) {
                header(HttpHeaders.Authorization, "Bearer $accessToken")
            }

        return checkReturnResult(response) { responseToTransform ->
            val spreadsheet: Spreadsheet = responseToTransform.body()
            spreadsheet.sheets.map { it.properties.title }
        }
    }

    fun closeClient() {
        httpClient.close()
    }

    private inline fun <T> checkReturnResult(
        response: HttpResponse,
        onFailureExceptionMessage: String? = null,
        onSuccessTransform: (HttpResponse) -> T,
    ): Result<T> {
        if (response.status.value in 200..299) {
            return Result.success(onSuccessTransform(response))
        }

        if (onFailureExceptionMessage != null)
            return Result.failure(Exception(onFailureExceptionMessage))

        return Result.failure(Exception(response.status.toString()))
    }
}