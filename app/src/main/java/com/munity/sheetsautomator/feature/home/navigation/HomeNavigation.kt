package com.munity.sheetsautomator.feature.home.navigation

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.munity.sheetsautomator.feature.home.HomeScreen
import com.munity.sheetsautomator.feature.home.HomeViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) =
    navigate(HomeRoute, navOptions)

fun NavGraphBuilder.homeScreen(
    context: Context,
    onShowSnackbar: suspend (String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    composable<HomeRoute> { navBackStackEntry ->
        val homeViewModel: HomeViewModel = viewModel(
            factory = HomeViewModel.Factory
        )

        val uiState by homeViewModel.uiState.collectAsState()
        val isLoggedIn by homeViewModel.isLoggedIn.collectAsState()
        val categories: List<String> by homeViewModel.categories.collectAsState()

        HomeScreen(
            isLoggedIn = isLoggedIn,
            amount = uiState.amount,
            onAmountChange = { homeViewModel.onAmountChange(it) },
            onDateChange = { homeViewModel.onDateChange(it) },
            category = uiState.category,
            onDropDownMenuItemClick = { homeViewModel.onDropDownMenuItemClick(it) },
            dropDownItems = categories,
            description = uiState.description,
            onDescriptionChange = { homeViewModel.onDescriptionChange(it) },
            onSignInButtonClick = { homeViewModel.onSignInButtonClick(context) },
            onAddButtonClick = {
                homeViewModel.viewModelScope.launch {
                    homeViewModel.onAddButtonClick()
                }
            },
            isSnackBarShowing = uiState.isSnackBarShowing,
            snackBarMessage = uiState.snackBarMessage,
            onShowSnackbar = onShowSnackbar,
            onDismissSnackBar = homeViewModel::onDismissSnackBar,
            modifier = modifier,
        )
    }
}
