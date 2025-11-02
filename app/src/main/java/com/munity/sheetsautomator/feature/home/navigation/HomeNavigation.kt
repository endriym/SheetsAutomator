package com.munity.sheetsautomator.feature.home.navigation

import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.munity.sheetsautomator.feature.home.HomeScreen
import com.munity.sheetsautomator.feature.home.HomeViewModel
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) =
    navigate(HomeRoute, navOptions)

fun NavGraphBuilder.homeScreen(
    context: Context,
    showSnackbar: suspend (String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    composable<HomeRoute> { navBackStackEntry ->
        val homeVM: HomeViewModel = viewModel(factory = HomeViewModel.Factory)

        val uiState by homeVM.uiState.collectAsState()
        val isLoggedIn by homeVM.isLoggedIn.collectAsState()
        val categories: List<String> by homeVM.categories.collectAsState()

        LaunchedEffect(key1 = true) {
            homeVM.snackbarMessages.collect { message ->
                message?.let {
                    showSnackbar(it)
                }
            }
        }

        HomeScreen(
            isLoggedIn = isLoggedIn,
            amount = uiState.amount ?: "",
            onAmountChange = homeVM::onAmountChange,
            selectedDate = uiState.date ?: "",
            onDateChange = homeVM::onDateChange,
            category = uiState.category ?: "",
            onDropDownMenuItemClick = homeVM::onDropDownMenuItemClick,
            dropDownItems = categories,
            description = uiState.description ?: "",
            onDescriptionChange = homeVM::onDescriptionChange,
            onSignInButtonClick = { homeVM.onSignInButtonClick(context) },
            onAddButtonClick = homeVM::onAddButtonClick,
            modifier = modifier,
        )
    }
}
