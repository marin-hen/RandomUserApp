package com.example.testapp.user.presentation.model

data class UserDetailsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val user: UserUiModel? = null
)