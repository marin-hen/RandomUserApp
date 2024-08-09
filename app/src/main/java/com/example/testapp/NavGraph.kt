package com.example.testapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.testapp.user.route.UserDetailsRoute
import com.example.testapp.user.route.UserListRoute
import com.example.testapp.user.route.userDetailsScreen
import com.example.testapp.user.route.userListScreen

@Composable
fun Graph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = UserListRoute.route,
        builder = {
            userListScreen(
                onItemDetailsClick = { id: Long ->
                    val params = UserDetailsRoute.Params(
                        userId = id,
                    )
                    navController.navigate(UserDetailsRoute.build(params))
                }
            )
            userDetailsScreen()
        }
    )
}