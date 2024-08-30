package com.example.testapp.user.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.testapp.user.presentation.screens.UserDetailsScreen
import com.example.testapp.user.presentation.screens.UserListScreen
import com.example.testapp.user.presentation.viewmodel.UserDetailsViewModel
import com.example.testapp.user.presentation.viewmodel.UsersListViewModel

fun NavGraphBuilder.userListScreen(
    onItemDetailsClick: (id: Long) -> Unit
) {
    composable<Screen.UserList> {
        val viewModel = hiltViewModel<UsersListViewModel>()
        val state by viewModel.state.collectAsState()
        val errorStateMessage by viewModel.errorStateMessage.collectAsState()

        UserListScreen(
            state = state,
            error = errorStateMessage,
            onItemDetailsClick = onItemDetailsClick,
            onFavoriteUserClick = viewModel::onFavoriteClick,
            onFavoriteFilterClick = viewModel::switchUserFilter,
            onRefresh = viewModel::refreshUsers,
            onClearError = viewModel::onClearErrorState
        )
    }
}

fun NavGraphBuilder.userDetailsScreen(
) {
    composable<Screen.UserDetails> {
        val viewModel = hiltViewModel<UserDetailsViewModel>()
        val state by viewModel.state.collectAsState()

        UserDetailsScreen(
            uiState = state,
        )
    }
}