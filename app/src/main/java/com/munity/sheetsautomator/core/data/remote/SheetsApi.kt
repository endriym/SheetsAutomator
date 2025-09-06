package com.munity.sheetsautomator.core.data.remote

import com.munity.sheetsautomator.BuildConfig
import com.munity.sheetsautomator.core.data.local.datastore.PreferencesStorage
import com.munity.sheetsautomator.core.data.remote.model.DriveFileResponse
import com.munity.sheetsautomator.core.data.remote.model.DriveGetFilesResponse
import com.munity.sheetsautomator.core.data.remote.model.ErrorInfo
import com.munity.sheetsautomator.core.data.remote.model.SpreadsheetResponse
import com.munity.sheetsautomator.core.data.remote.model.TokenInfo
import com.munity.sheetsautomator.core.data.remote.model.ValueRange
import com.munity.sheetsautomator.util.DateUtil
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.net.URLEncoder

class SheetsApi(private val prefsStorage: PreferencesStorage) {
    companion object {
        private const val CLIENT_ID = BuildConfig.CLIENT_ID
        private const val PACKAGE_NAME = "com.munity.sheetsautomator"
        private const val TAG = "SheetsDataSource"
    }

    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val storedPrefs = prefsStorage.storedPreferences.first()
                    val accessToken = storedPrefs.accessToken
                    val refreshToken = storedPrefs.refreshToken
                    val expiration = storedPrefs.expirationDate

                    if (expiration != null && DateUtil.isIso8601Expired(expiration))
                        return@loadTokens BearerTokens(accessToken!!, refreshToken)

                    if (accessToken != null)
                        return@loadTokens BearerTokens(accessToken, refreshToken)

                    return@loadTokens BearerTokens("", refreshToken)
                }

                refreshTokens {
                    val tokenInfoResult =
                        getAccessRefreshTokens(this.oldTokens?.refreshToken!!, isRefresh = true)

                    tokenInfoResult.onSuccess { newTokenInfo ->
                        val expirationDate: String =
                            DateUtil.getIso8601FromNowIn(newTokenInfo.expiresIn)
                        // Save new token
                        prefsStorage.saveAccessToken(newTokenInfo.accessToken, expirationDate)
                        prefsStorage.saveRefreshToken(newTokenInfo.refreshToken!!)

                        return@refreshTokens BearerTokens(
                            newTokenInfo.accessToken,
                            newTokenInfo.refreshToken
                        )
                    }

//                    throw IllegalStateException("Login needs to be redone!")
                    null
                }

                // return false for urls which don't need an Authorization header
                // (ex: token or refresh token urls),
                // otherwise return true to include the Authorization header
                sendWithoutRequest { request ->
                    request.url.buildString() != SheetsEndpoint.GOOGLE_ACCESS_REFRESH_ENDPOINT
                }
            }
        }
    }

    suspend fun getAccessRefreshTokens(
        authorizationCode: String,
        isRefresh: Boolean,
    ): Result<TokenInfo> {
        val response: HttpResponse = httpClient.submitForm(
            url = SheetsEndpoint.GOOGLE_ACCESS_REFRESH_ENDPOINT,
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

    suspend fun getSpreadsheetFiles(): Result<List<DriveFileResponse>> {
        val response = httpClient.get(urlString = SheetsEndpoint.GOOGLE_DRIVE_SPREADSHEETS_ENDPOINT)

        return checkReturnResult(response) { responseToTransform ->
            val driveGetFilesResponse = responseToTransform.body<DriveGetFilesResponse>()
            driveGetFilesResponse.files
        }
    }

    suspend fun getSheetTitles(spreadsheetID: String): Result<List<String>> {
        val response =
            httpClient.get(
                urlString = SheetsEndpoint.GOOGLE_SPREADSHEET_ENDPOINT.format(
                    spreadsheetID
                )
            )

        return checkReturnResult(response) { responseToTransform ->
            val spreadsheetResponse: SpreadsheetResponse = responseToTransform.body()
            spreadsheetResponse.sheetResponses.map { it.properties.title }
        }
    }

    suspend fun getCategories(
        spreadsheetID: String,
        sheetTitle: String,
        range: String,
    ): Result<List<String>> {
        val response = httpClient.get(
            urlString = SheetsEndpoint.GOOGLE_SHEETS_VALUES_ENDPOINT.format(
                spreadsheetID,
                URLEncoder.encode("'$sheetTitle'!$range", Charsets.UTF_8.name()),
            )
        )

        return checkReturnResult(response) { responseToTransform ->
            responseToTransform.body<ValueRange>().values.flatten()
        }
    }

    suspend fun appendRows(
        spreadsheetID: String,
        range: String,
        valueRange: ValueRange,
    ): Result<Unit> {
        val response =
            httpClient.post(
                urlString = SheetsEndpoint.GOOGLE_SHEETS_APPEND_ENDPOINT.format(
                    spreadsheetID,
                    range
                ),
            ) {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(valueRange))
            }

        return checkReturnResult(response) {}
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
