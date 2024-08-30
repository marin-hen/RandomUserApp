package com.example.testapp.user.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object UserList : Screen()

    @Serializable
    data class UserDetails(
        val userId: Long? = null
    ) : Screen()
}