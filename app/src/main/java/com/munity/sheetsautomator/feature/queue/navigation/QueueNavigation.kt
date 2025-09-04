package com.munity.sheetsautomator.feature.queue.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.munity.sheetsautomator.feature.queue.QueueScreen
import kotlinx.serialization.Serializable

@Serializable
data object QueueRoute

fun NavController.navigateToQueue(navOptions: NavOptions? = null) =
    navigate(QueueRoute, navOptions)

fun NavGraphBuilder.queueScreen(
    onShowSnackbar: suspend (String) -> Boolean,
    modifier: Modifier = Modifier,
) {
    composable<QueueRoute> { navBackStackEntry ->
        QueueScreen(

        )
    }
}

