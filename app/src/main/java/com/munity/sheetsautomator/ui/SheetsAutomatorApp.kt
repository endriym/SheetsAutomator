package com.munity.sheetsautomator.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.munity.sheetsautomator.core.ui.components.SheetsBottomAppBar
import com.munity.sheetsautomator.core.ui.components.SheetsTopAppBar
import com.munity.sheetsautomator.core.ui.theme.SheetsAutomatorTheme
import com.munity.sheetsautomator.feature.home.navigation.HomeRoute
import com.munity.sheetsautomator.feature.home.navigation.navigateToHome
import com.munity.sheetsautomator.feature.settings.navigation.navigateToSettings
import com.munity.sheetsautomator.navigation.SheetsNavHost
import com.munity.sheetsautomator.navigation.TopLevelDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetsAutomatorApp() {
    SheetsAutomatorTheme {
        val snackBarHostState = remember { SnackbarHostState() }
        val navHostController = rememberNavController()
        var selectedItem by remember { mutableIntStateOf(TopLevelDestination.HOME.ordinal) }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = { SheetsTopAppBar() },
            bottomBar = {
                SheetsBottomAppBar(
                    selectedItem = selectedItem,
                    topLevelDestinationEntries = TopLevelDestination.entries,
                    onEntryClick = { selectedIndex ->
                        selectedItem = selectedIndex

                        val topLevelNavOptions = navOptions {
//                            // Pop up to the start destination of the graph to
//                            // avoid building up a large stack of destinations
//                            // on the back stack as users select items
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }

                        when (selectedIndex) {
                            TopLevelDestination.HOME.ordinal -> navHostController.navigateToHome(
                                topLevelNavOptions
                            )

                            TopLevelDestination.QUEUE.ordinal -> TODO()

                            TopLevelDestination.SETTINGS.ordinal -> navHostController.navigateToSettings(
                                navOptions = topLevelNavOptions
                            )
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            val context = LocalContext.current

            SheetsNavHost(
                startDestination = HomeRoute,
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
