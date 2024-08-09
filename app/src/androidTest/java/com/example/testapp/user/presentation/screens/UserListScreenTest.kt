package com.example.testapp.user.presentation.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.testapp.user.presentation.model.ErrorUiMessage
import com.example.testapp.user.presentation.model.UserListUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testUserListScreenLoadingState() {
        composeTestRule.setContent {
            UserListScreen(
                state = UserListUiState.LoadingUiState,
                error = ErrorUiMessage(null),
                isFavoriteUsersEnabled = false,
                onItemDetailsClick = {},
                onFavoriteUserClick = { _, _ -> },
                onFavoriteFilterClick = {},
                onRefresh = {},
                onClearError = {}
            )
        }
        composeTestRule.onNodeWithTag("loading").assertExists()
    }

    @Test
    fun testUserListScreenUsersState() {
        composeTestRule.setContent {
            UserListScreen(
                state = mockUserListState,
                error = ErrorUiMessage(null),
                isFavoriteUsersEnabled = false,
                onItemDetailsClick = {},
                onFavoriteUserClick = { _, _ -> },
                onFavoriteFilterClick = {},
                onRefresh = {},
                onClearError = {}
            )
        }

        // Verify that the list is displayed
        composeTestRule.onNodeWithTag("user_list").assertExists()

        // Verify first item
        composeTestRule.onNodeWithText(mockUserListState.users[0].name)
            .assertExists()
    }

    @Test
    fun testUserListScreenErrorState() {
        val errorMessage = "Something went terribly wrong!"
        composeTestRule.setContent {
            UserListScreen(
                state = UserListUiState.ErrorUiState(errorMessage),
                error = ErrorUiMessage(null),
                isFavoriteUsersEnabled = false,
                onItemDetailsClick = {},
                onFavoriteUserClick = { _, _ -> },
                onFavoriteFilterClick = {},
                onRefresh = {},
                onClearError = {}
            )
        }
        // Verify that the error message is displayed
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }

    @Test
    fun testUserListScreenFavoriteFilterButton() {
        composeTestRule.setContent {
            UserListScreen(
                state = mockUserListState,
                error = ErrorUiMessage(null),
                isFavoriteUsersEnabled = false,
                onItemDetailsClick = {},
                onFavoriteUserClick = { _, _ -> },
                onFavoriteFilterClick = {},
                onRefresh = {},
                onClearError = {}
            )
        }

        composeTestRule.onNodeWithTag("test_tag_favorite_toggle").assertExists()
    }
}