package com.example.testapp.user.presentation.model

import androidx.annotation.StringRes

data class UserListUiState(
    val users: List<UserUiModel> = emptyList(),
    val isRefreshing: Boolean = false,
    val isFavoriteUsersEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    @StringRes val remoteErrorMessage: Int? = null
)