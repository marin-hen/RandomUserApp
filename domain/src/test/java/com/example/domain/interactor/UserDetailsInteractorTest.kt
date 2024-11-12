package com.example.domain.interactor

import com.example.domain.UsersRepository
import com.example.domain.model.UserDomainModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UserDetailsInteractorTest {

    @MockK
    private lateinit var usersRepository: UsersRepository

    private lateinit var classUnderTest: UserDetailsInteractor

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        classUnderTest = UserDetailsInteractor(testDispatcher, usersRepository)
    }

    @Test
    fun `getUserByIdAsFlow returns correct user`() = runTest(testDispatcher) {
        // Arrange
        val userId = 1L
        val expectedUser = mockk<UserDomainModel>(relaxed = true)
        val userFlow: Flow<UserDomainModel> = flow { emit(expectedUser) }

        every { usersRepository.getUserByIdAsFlow(userId) } returns userFlow

        // Act
        val result = classUnderTest.getUserByIdAsFlow(userId).toList()

        // Assert
        assertEquals(listOf(expectedUser), result)
        // Verify that the method was called
        verify(exactly = 1) { usersRepository.getUserByIdAsFlow(userId) }
    }
}