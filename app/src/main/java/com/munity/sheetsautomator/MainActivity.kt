package com.munity.sheetsautomator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.munity.sheetsautomator.ui.SheetsAutomatorApp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SheetsAutomatorApp()
        }
    }

    override fun onResume() {
        super.onResume()

        /**
         * Intercept the authentication code (if there is any) from intent's data.
         * This code will be valid for a short period,
         * and you can exchange it for access and refresh tokens.
         */
        intent.data.let {
            lifecycleScope.launch {
                (applicationContext as SheetsAutomatorApplication).sheetsRepository.exchangeAuthCode(
                    it.toString()
                )
            }
        }
    }
}