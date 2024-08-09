package com.example.testapp.user.route

import android.net.Uri
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.testapp.user.presentation.screens.UserDetailsScreen
import com.example.testapp.user.presentation.viewModel.UserDetailsViewModel

object UserDetailsRoute : NavRoute<UserDetailsRoute.Params>() {

    private const val PATH = "user/details"
    const val USER_ID = "id"
    override val route: String = "$PATH?$USER_ID={$USER_ID}"

    override fun build(params: Params): String {
        return Uri.Builder()
            .path(PATH)
            .appendQueryParameter(USER_ID, params.userId.toString())
            .build()
            .toString()
    }

    class Params(
        val userId: Long
    ) : NavParams
}

fun NavGraphBuilder.userDetailsScreen(
) {
    composable(
        UserDetailsRoute.route,
        arguments = listOf(
            navArgument(UserDetailsRoute.USER_ID) { },
        ),
    ) {
        val viewModel = hiltViewModel<UserDetailsViewModel>()
        val state by viewModel.state.collectAsState()

        UserDetailsScreen(
            uiState = state,
        )
    }
}