package com.example.user.presentation.viewmodel

import com.example.domain.interactor.UsersInteractor
import com.example.domain.model.LocationModel
import com.example.domain.model.PictureModel
import com.example.domain.model.StreetModel
import com.example.domain.model.UserDomainModel
import com.example.user.presentation.viewModel.UsersListViewModel
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class UsersListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val usersInteractor = mockk<UsersInteractor>(relaxed = true)

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

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun `test UsersListViewModel initial state`() = testScope.runTest {
        // Arrange
        val usersListViewModel = UsersListViewModel(usersInteractor)

        // Act
        val emittedStates = usersListViewModel.state.take(1).toList()

        // Assert
        assertTrue(emittedStates[0].isLoading)
    }

    @Test
    fun `test UsersListViewModel load users successfully`() = testScope.runTest {
        // Arrange
        coEvery { usersInteractor.getUsersAsFlow(any()) } returns flowOf(userDomainModels)
        coEvery { usersInteractor.fetchUsers(any()) } just Runs

        val usersListViewModel = UsersListViewModel(usersInteractor)

        // Act
        val emittedStates = usersListViewModel.state.take(3).toList()

        // Assert
        assertTrue(emittedStates[0].isLoading)
        assertFalse(emittedStates[1].isLoading)
        assertTrue(emittedStates[2].users.isNotEmpty())
    }

    @Test
    fun `test UsersListViewModel error state`() = testScope.runTest {
        // Arrange
        val errorMessage = "An error occurred"
        coEvery { usersInteractor.getUsersAsFlow(any()) } returns flow {
            throw Exception(errorMessage)
        }

        val usersListViewModel = UsersListViewModel(usersInteractor)

        // Act
        val emittedStates = usersListViewModel.state.take(3).toList()

        // Assert
        assertTrue(emittedStates[0].isLoading)
        assertFalse(emittedStates[1].isLoading)
        assertNotNull(emittedStates[2].errorMessage)
    }

    @Test
    fun `test UsersListViewModel switch user filter`() = testScope.runTest {
        // Arrange
        coEvery { usersInteractor.getUsersAsFlow(any()) } returns flowOf(userDomainModels)
        coEvery { usersInteractor.fetchUsers(any()) } just Runs

        val usersListViewModel = UsersListViewModel(usersInteractor)

        // Act
        usersListViewModel.switchUserFilter(true)
        val emittedStates = usersListViewModel.state.take(3).toList()

        // Assert
        assertTrue(emittedStates[0].isLoading)
        assertFalse(emittedStates[1].isLoading)
        assertTrue(emittedStates[2].users.isNotEmpty())
    }

    @Test
    fun `test UsersListViewModel refresh users`() = testScope.runTest {
        // Arrange
        coEvery { usersInteractor.getUsersAsFlow(any()) } returns flowOf(userDomainModels)
        coEvery { usersInteractor.fetchUsers(any()) } just Runs

        val usersListViewModel = UsersListViewModel(usersInteractor)

        // Act
        usersListViewModel.refreshUsers()
        val emittedStates = usersListViewModel.state.take(3).toList()

        // Assert
        assertTrue(emittedStates[0].isLoading)
        assertTrue(emittedStates[1].isRefreshing)
        assertTrue(emittedStates[2].users.isNotEmpty())
    }

    @Test
    fun `test UsersListViewModel onFavoriteClick updates favorite status`() = testScope.runTest {
        // Arrange
        val userId = 1L
        val isFavorite = true
        coEvery { usersInteractor.updateIsFavoriteById(userId, isFavorite) } just Runs

        val usersListViewModel = UsersListViewModel(usersInteractor)

        // Act
        usersListViewModel.onFavoriteClick(userId, isFavorite)
    }
}
