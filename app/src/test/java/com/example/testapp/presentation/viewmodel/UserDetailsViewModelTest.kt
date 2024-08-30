package com.example.testapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.testapp.user.domain.interactor.UserDetailsInteractor
import com.example.testapp.user.domain.model.LocationModel
import com.example.testapp.user.domain.model.PictureModel
import com.example.testapp.user.domain.model.StreetModel
import com.example.testapp.user.domain.model.UserDomainModel
import com.example.testapp.user.navigation.Screen
import com.example.testapp.user.presentation.model.UserDetailsUiState
import com.example.testapp.user.presentation.model.toUiModel
import com.example.testapp.user.presentation.viewmodel.UserDetailsViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class UserDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private val userDetailsInteractor = mockk<UserDetailsInteractor>(relaxed = true)

    private val userDomainModel = UserDomainModel(
        id = 123L,
        firstName = "John",
        lastName = "Doe",
        gender = "Male",
        location = LocationModel(
            city = "New York", country = "USA", street = StreetModel(
                number = 615,
                name = "Broadway"
            ),
            timezone = ""
        ),
        email = "john.doe@example.com",
        phone = "123-456-7890",
        cell = "098-765-4321",
        idName = "SSN",
        idValue = "123-45-6789",
        picture = PictureModel(
            large = "large_pic_url",
            medium = "medium_pic_url",
            thumbnail = "thumbnail_pic_url"
        ),
        isFavorite = true
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    // todo doesn't work because of https://issuetracker.google.com/issues/349807172?pli=1
    // https://issuetracker.google.com/issues/340966212?hl=it
    // https://issuetracker.google.com/issues/349807172
    // https://slack-chats.kotlinlang.org/t/18821653/previously-for-unit-testing-viewmodels-that-depend-on-a-save


    @Ignore()
    @Test
    fun `test UserDetailsViewModel with valid userId`() = testScope.runTest {
        // Arrange
        val userId = 123L
        val userUiState = UserDetailsUiState(user = userDomainModel.toUiModel())
        every { savedStateHandle.toRoute<Screen.UserDetails>().userId } returns userId
        coEvery { userDetailsInteractor.getUserByIdAsFlow(userId) } returns flowOf(userDomainModel)
        // Act
        val viewModel = UserDetailsViewModel(savedStateHandle, userDetailsInteractor)

        // Assert
        val emittedStates = viewModel.state.take(2).toList()
        assertTrue(emittedStates[0].isLoading)
        assert(emittedStates[1] == userUiState)
    }

    @Ignore
    @Test
    fun `test UserDetailsViewModel with missing userId`() = testScope.runTest {
        // Arrange
        every { savedStateHandle.toRoute<Screen.UserDetails>().userId } returns null

        // Act
        val viewModel = UserDetailsViewModel(savedStateHandle, userDetailsInteractor)

        // Assert
        val emittedStates = viewModel.state.take(2).toList()
        assertTrue(emittedStates[0].isLoading)
        assertNotNull(emittedStates[1].errorMessage)
    }
}