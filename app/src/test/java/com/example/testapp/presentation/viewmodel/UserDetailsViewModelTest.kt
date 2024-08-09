package com.example.testapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.testapp.user.domain.model.LocationModel
import com.example.testapp.user.domain.model.PictureModel
import com.example.testapp.user.domain.model.StreetModel
import com.example.testapp.user.domain.model.UserDomainModel
import com.example.testapp.user.domain.usecase.UserDetailsUseCase
import com.example.testapp.user.presentation.model.UserDetailsUiState
import com.example.testapp.user.presentation.model.toUiModel
import com.example.testapp.user.presentation.viewModel.UserDetailsViewModel
import com.example.testapp.user.route.UserDetailsRoute
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
import org.junit.Before
import org.junit.Test

class UserDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
    private val userDetailsUseCase = mockk<UserDetailsUseCase>(relaxed = true)

    private val userDomainModel = UserDomainModel(
        id = 1L,
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

    @Test
    fun `test UserDetailsViewModel with valid userId`() = testScope.runTest {
        // Arrange
        val userId = "123"
        val userUiState = UserDetailsUiState.UserUiState(user = userDomainModel.toUiModel())
        every { savedStateHandle.get<String>(UserDetailsRoute.USER_ID) } returns userId
        coEvery { userDetailsUseCase.getUserByIdAsFlow(123L) } returns flowOf(userDomainModel)
        // Act
        val viewModel = UserDetailsViewModel(savedStateHandle, userDetailsUseCase)

        // Assert
        val emittedStates = viewModel.state.take(2).toList()
        assert(emittedStates[0] is UserDetailsUiState.LoadingUiState)
        assert(emittedStates[1] == userUiState)
    }

    @Test
    fun `test UserDetailsViewModel with missing userId`() = testScope.runTest {
        // Arrange
        every { savedStateHandle.get<String>(UserDetailsRoute.USER_ID) } returns null

        // Act
        val viewModel = UserDetailsViewModel(savedStateHandle, userDetailsUseCase)

        // Assert
        val emittedStates = viewModel.state.take(2).toList()
        assert(emittedStates[0] is UserDetailsUiState.LoadingUiState)
        assert(emittedStates[1] is UserDetailsUiState.ErrorUiState)
    }
}