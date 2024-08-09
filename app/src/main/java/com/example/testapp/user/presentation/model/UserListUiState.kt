package com.example.testapp.user.presentation.model

sealed interface UserListUiState {
    data object LoadingUiState : UserListUiState
    data class ErrorUiState(val errorMessage: String?) : UserListUiState
    data class UsersUiState(
        val users: List<UserUiModel> = emptyList(),
        val isRefreshing: Boolean = false
    ) : UserListUiState
}