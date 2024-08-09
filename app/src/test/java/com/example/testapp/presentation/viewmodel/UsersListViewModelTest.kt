package com.example.testapp.presentation.viewmodel

import com.example.testapp.user.domain.model.LocationModel
import com.example.testapp.user.domain.model.PictureModel
import com.example.testapp.user.domain.model.StreetModel
import com.example.testapp.user.domain.model.UserDomainModel
import com.example.testapp.user.domain.usecase.UsersUseCase
import com.example.testapp.user.presentation.model.UserListUiState
import com.example.testapp.user.presentation.viewModel.UsersListViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
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

class UsersListViewModelTest {


    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val usersUseCase = mockk<UsersUseCase>(relaxed = true)

    private val userDomainModels = listOf(
        UserDomainModel(
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
    fun `test UsersListViewModel initial state`() = testScope.runTest {
        // Arrange
        val usersListViewModel = UsersListViewModel(usersUseCase)

        // Act
        val emittedStates = usersListViewModel.state.take(1).toList()

        // Assert
        assert(emittedStates[0] is UserListUiState.LoadingUiState)
    }

    @Test
    fun `test UsersListViewModel load users`() = testScope.runTest {
        // Arrange

        coEvery { usersUseCase.getUsersAsFlow(any()) } returns flowOf(userDomainModels)
        coEvery { usersUseCase.fetchUsers(any()) } just Runs

        val usersListViewModel = UsersListViewModel(usersUseCase)

        // Act
        usersListViewModel.fetchUsers()

        val emittedStates = usersListViewModel.state.take(2).toList()

        // Assert
        assert(emittedStates[0] is UserListUiState.LoadingUiState)
        assert(emittedStates[1] is UserListUiState.UsersUiState)
        val usersUiState = emittedStates[1] as UserListUiState.UsersUiState
        assert(usersUiState.users.isNotEmpty())
    }

    @Test
    fun `test UsersListViewModel error state`() = testScope.runTest {
        // Arrange
        val errorMessage = "An error occurred"
        coEvery { usersUseCase.getUsersAsFlow(any()) } returns flow {
            throw Exception(errorMessage)
        }

        val usersListViewModel = UsersListViewModel(usersUseCase)

        // Act
        usersListViewModel.fetchUsers()

        val emittedStates = usersListViewModel.state.take(2).toList()

        // Assert
        assert(emittedStates[0] is UserListUiState.LoadingUiState)
        assert(emittedStates[1] is UserListUiState.ErrorUiState)
        val errorUiState = emittedStates[1] as UserListUiState.ErrorUiState
        assert(errorUiState.errorMessage == errorMessage)
    }

    @Test
    fun `test UsersListViewModel switch user filter`() = testScope.runTest {
        // Arrange

        coEvery { usersUseCase.getUsersAsFlow(any()) } returns flowOf(userDomainModels)
        coEvery { usersUseCase.fetchUsers(any()) } just Runs

        val usersListViewModel = UsersListViewModel(usersUseCase)

        // Act
        usersListViewModel.switchUserFilter(true)

        val emittedStates = usersListViewModel.state.take(2).toList()

        // Assert
        assert(emittedStates[0] is UserListUiState.LoadingUiState)
        assert(emittedStates[1] is UserListUiState.UsersUiState)
        val usersUiState = emittedStates[1] as UserListUiState.UsersUiState
        assert(usersUiState.users.isNotEmpty())
    }
}