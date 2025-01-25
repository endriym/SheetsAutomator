package com.munity.sheetsautomator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.munity.sheetsautomator.feature.home.HomeScreen
import com.munity.sheetsautomator.feature.home.HomeViewModel

@Composable
fun SheetsNavHost(startDestination: String, modifier: Modifier = Modifier) {
    NavHost(
        navController = rememberNavController(),
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = "home") { navBackStackEntry ->
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.Factory
            )

            val uiState by homeViewModel.uiState.collectAsState()
            val isLoggedIn by homeViewModel.isLoggedIn.collectAsState()

            HomeScreen(
                loggedIn = isLoggedIn,
                amount = uiState.amount,
                onAmountChange = { homeViewModel.onAmountChange(it) },
                onDateChange = { homeViewModel.onDateChange(it) },
                category = uiState.category,
                onDropDownMenuItemClick = { homeViewModel.onDropDownMenuItemClick(it) },
                dropDownItems = listOf(),
                description = uiState.description,
                onDescriptionChange = { homeViewModel.onDescriptionChange(it) },
                onSignInButtonClick = { homeViewModel.onSignInButtonClick() },
                onAddButtonClick = { homeViewModel.onAddButtonClick() },
                modifier = modifier,
            )
        }
    }
}