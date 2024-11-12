package com.example.testapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.user.navigation.Screen
import com.example.user.navigation.userDetailsScreen
import com.example.user.navigation.userListScreen

@Composable
fun Graph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.UserList,
        builder = {
            userListScreen(
                onItemDetailsClick = { id: Long ->
                    navController.navigate(Screen.UserDetails(userId = id))
                }
            )
            userDetailsScreen()
        }
    )
}