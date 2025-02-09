package com.munity.sheetsautomator.core.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.munity.sheetsautomator.SheetsNavHost
import com.munity.sheetsautomator.core.ui.components.SheetsBottomAppBar
import com.munity.sheetsautomator.core.ui.components.SheetsTopAppBar
import com.munity.sheetsautomator.feature.home.navigation.navigateToHome
import com.munity.sheetsautomator.feature.settings.navigation.navigateToSettings
import com.munity.sheetsautomator.ui.theme.SheetsAutomatorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetsAutomatorApp() {
    SheetsAutomatorTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        val navHostController = rememberNavController()

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = { SheetsTopAppBar() },
            bottomBar = {
                SheetsBottomAppBar(
                    onHomeButtonClick = { navHostController.navigateToHome() },
                    onSettingsButtonClick = { navHostController.navigateToSettings() }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            val context = LocalContext.current
            SheetsNavHost(
                startDestination = "home",
                navHostController = navHostController,
                onShowSnackbar = { message ->
                    snackBarHostState.showSnackbar(message = message) == SnackbarResult.Dismissed
                },
                context = context,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
