package com.munity.sheetsautomator

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.munity.sheetsautomator.feature.home.navigation.homeScreen
import com.munity.sheetsautomator.feature.settings.navigation.settingsScreen

@Composable
fun SheetsNavHost(
    startDestination: String,
    navHostController: NavHostController,
    onShowSnackbar: suspend (String) -> Boolean,
    context: Context,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        homeScreen(context, onShowSnackbar)
        settingsScreen(onShowSnackbar)
    }
}