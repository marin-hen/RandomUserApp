package com.example.testapp.user.presentation.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class UserListUiState(
    val users: ImmutableList<UserUiModel> = persistentListOf(),
    val isRefreshing: Boolean = false,
    val isFavoriteUsersEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)