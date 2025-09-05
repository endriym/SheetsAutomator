package com.munity.sheetsautomator.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.munity.sheetsautomator.BuildConfig

object OAuthUtil {
    private const val CLIENT_ID = BuildConfig.CLIENT_ID
    const val GOOGLE_SHEETS_SCOPE = "https://www.googleapis.com/auth/spreadsheets"
    const val GOOGLE_DRIVE_SCOPE = "https://www.googleapis.com/auth/drive.readonly"
    private const val GOOGLE_AUTHENTICATION_CODE_ENDPOINT =
        "https://accounts.google.com/o/oauth2/v2/auth/oauth2redirect"
    private const val PACKAGE_NAME = "com.munity.sheetsautomator"

    /**
     * Launches the default browser (as configured in the user's device settings) to
     * authenticate the user with their Google Account.
     *
     * @param context is needed to start the browser activity with an Intent.
     */
    fun launchAuthentication(context: Context) {
        val googleEndpointUri =
            GOOGLE_AUTHENTICATION_CODE_ENDPOINT.toUri().buildUpon()
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("redirect_uri", "$PACKAGE_NAME:")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("scope", "$GOOGLE_SHEETS_SCOPE $GOOGLE_DRIVE_SCOPE")
                .build()

        val intent = Intent(Intent.ACTION_VIEW, googleEndpointUri)
        context.startActivity(intent)
    }
}
