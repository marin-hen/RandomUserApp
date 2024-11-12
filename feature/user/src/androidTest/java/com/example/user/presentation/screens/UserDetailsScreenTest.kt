package com.example.user.presentation.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.user.presentation.model.PictureUiModel
import com.example.user.presentation.model.UserDetailsUiState
import com.example.user.presentation.model.UserUiModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockUser = UserUiModel(
        id = 1L,
        name = "John Doe",
        location = "New York",
        gender = "Unknown",
        picture = PictureUiModel(thumbnail = "", large = "", medium = ""),
        email = "test@gmail.com",
        phone = "555 55 55",
        cell = "+45 666 666 66",
        timezone = "Adelaide, Darwin",
        isFavorite = false
    )

    @Test
    fun userDetailsScreen_displaysUserDetails() {

        composeTestRule.setContent {
            UserDetailsScreen(uiState = UserDetailsUiState(user = mockUser))
        }

        composeTestRule.onNodeWithText("User Details")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(mockUser.name)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(mockUser.location)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(mockUser.email)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(mockUser.phone)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(mockUser.cell)
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(mockUser.gender).performScrollTo()
            .assertIsDisplayed()

        composeTestRule.onNodeWithText(mockUser.timezone)
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("zoom_in")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithTag("large_image_dialog")
            .assertIsDisplayed()
    }

    @Test
    fun userDetailsScreen_displaysErrorState() {
        val errorMessage = "Something went terribly wrong!"

        composeTestRule.setContent {
            UserDetailsScreen(uiState = UserDetailsUiState(errorMessage = errorMessage))
        }

        composeTestRule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun userDetailsScreen_displaysLoadingState() {
        composeTestRule.setContent {
            UserDetailsScreen(uiState = UserDetailsUiState(isLoading = true))
        }

        composeTestRule.onNodeWithTag("loading")
            .assertExists()
    }
}