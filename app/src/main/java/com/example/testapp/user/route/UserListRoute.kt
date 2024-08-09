package com.example.testapp.user.route

import android.net.Uri
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.testapp.user.presentation.screens.UserListScreen
import com.example.testapp.user.presentation.viewModel.UsersListViewModel

object UserListRoute : NavRoute<EmptyNavParams>() {

    private const val PATH = "user/list"
    override val route: String = PATH

    override fun build(params: EmptyNavParams): String {
        return Uri.Builder()
            .path(PATH)
            .build()
            .toString()
    }
}

fun NavGraphBuilder.userListScreen(
    onItemDetailsClick: (id: Long) -> Unit
) {
    composable(UserListRoute.route) {
        val viewModel = hiltViewModel<UsersListViewModel>()
        val state by viewModel.state.collectAsState()
        val errorStateMessage by viewModel.errorStateMessage.collectAsState()
        val isFavoriteUsersEnabled by viewModel.isFavoriteUsersEnabled.collectAsState()

        UserListScreen(
            state = state,
            error = errorStateMessage,
            isFavoriteUsersEnabled = isFavoriteUsersEnabled,
            onItemDetailsClick = onItemDetailsClick,
            onFavoriteUserClick = viewModel::onFavoriteClick,
            onFavoriteFilterClick = viewModel::switchUserFilter,
            onRefresh = viewModel::fetchUsers,
            onClearError = viewModel::onClearErrorState
        )
    }
}