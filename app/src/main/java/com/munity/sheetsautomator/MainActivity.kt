package com.munity.sheetsautomator

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.munity.sheetsautomator.ui.theme.SheetsAutomatorTheme
import com.munity.sheetsautomator.util.OAuthUtil
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SheetsAutomatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SheetsAutomatorApp(
                        context = this,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        /**
         * Intercept the authentication code (if there is any).
         * This code will be valid for a short period (around 10 minutes),
         * and you can exchange it for access and refresh tokens.
         */
        intent.data.let {
            lifecycleScope.launch {
                val authCode = OAuthUtil.extractAuthCode(applicationContext, it.toString())
                authCode?.let {
                    OAuthUtil.getAccessRefreshTokens(applicationContext, it)
                }
            }
        }
    }
}

@Composable
fun SheetsAutomatorApp(context: Context, modifier: Modifier = Modifier) {
    SheetsNavHost("home", context, modifier)
}