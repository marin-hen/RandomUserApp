package com.example.testapp.user.presentation.model

sealed interface UserDetailsUiState {
    data class LoadingUiState(val isLoading: Boolean = true) : UserDetailsUiState
    data class ErrorUiState(val errorMessage: String?) : UserDetailsUiState
    data class UserUiState(
        val user: UserUiModel
    ) : UserDetailsUiState
}