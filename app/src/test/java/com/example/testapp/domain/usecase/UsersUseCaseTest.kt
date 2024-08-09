package com.example.testapp.domain.usecase

import com.example.testapp.user.domain.UsersRepository
import com.example.testapp.user.domain.model.UserDomainModel
import com.example.testapp.user.domain.usecase.UsersUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UsersUseCaseTest {

    @MockK
    private lateinit var usersRepository: UsersRepository

    private lateinit var classUnderTest: UsersUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        classUnderTest = UsersUseCase(testDispatcher, usersRepository)
    }

    @Test
    fun `fetchUsers calls repository with correct request`() = runTest(testDispatcher) {
        // Arrange
        val request = 10
        coEvery { usersRepository.fetchUsers(request) } returns Unit

        // Act
        classUnderTest.fetchUsers(request)

        // Assert
        coVerify(exactly = 1) { usersRepository.fetchUsers(request) }
    }

    @Test
    fun `getUsersAsFlow returns favorite users when enabled`() = runTest(testDispatcher) {
        // Arrange
        val isFavoriteUsersEnabled = true
        val favoriteUsers = listOf(mockk<UserDomainModel>(relaxed = true))
        val favoriteUsersFlow: Flow<List<UserDomainModel>> = flow { emit(favoriteUsers) }

        every { usersRepository.getFavoriteUsersAsFlow() } returns favoriteUsersFlow

        // Act
        val result = classUnderTest.getUsersAsFlow(isFavoriteUsersEnabled).toList()

        // Assert
        assertEquals(listOf(favoriteUsers), result)
        coVerify(exactly = 1) { usersRepository.getFavoriteUsersAsFlow() }
    }

    @Test
    fun `getUsersAsFlow returns all users when favorite users is disabled`() =
        runTest(testDispatcher) {
            // Arrange
            val isFavoriteUsersEnabled = false
            val allUsers = listOf(mockk<UserDomainModel>(relaxed = true))
            val allUsersFlow: Flow<List<UserDomainModel>> = flow { emit(allUsers) }

            every { usersRepository.getUsersAsFlow() } returns allUsersFlow

            // Act
            val result = classUnderTest.getUsersAsFlow(isFavoriteUsersEnabled).toList()

            // Assert
            assertEquals(listOf(allUsers), result)
            coVerify(exactly = 1) { usersRepository.getUsersAsFlow() }
        }

    @Test
    fun `updateIsFavoriteById calls repository with correct parameters`() =
        runTest(testDispatcher) {
            // Arrange
            val userId = 1L
            val isFavorite = true
            coEvery { usersRepository.updateIsFavoriteById(userId, isFavorite) } returns Unit

            // Act
            classUnderTest.updateIsFavoriteById(userId, isFavorite)

            // Assert
            coVerify(exactly = 1) { usersRepository.updateIsFavoriteById(userId, isFavorite) }
        }
}