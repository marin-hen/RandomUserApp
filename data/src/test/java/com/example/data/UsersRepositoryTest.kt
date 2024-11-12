package com.example.data

import com.example.domain.UsersRepository

import com.example.data.local.UsersDao
import com.example.data.local.model.UserEntity
import com.example.data.local.model.toDomain
import com.example.data.remote.UsersApi
import com.example.data.remote.model.Location
import com.example.data.remote.model.Picture
import com.example.data.remote.model.Street
import com.example.data.remote.model.Timezone
import com.example.data.remote.model.UsersDto
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UsersRepositoryTest {

    @MockK
    private lateinit var usersApi: UsersApi

    @MockK
    private lateinit var usersDao: UsersDao

    private lateinit var usersRepository: UsersRepository

    private val userEntity = UserEntity(
        id = 1L,
        firstName = "John",
        lastName = "Doe",
        gender = "Male",
        location = Location(
            city = "New York", country = "USA", street = Street(
                number = 615,
                name = "Broadway"
            ), timezone = Timezone(offset = "", description = "")
        ),
        email = "john.doe@example.com",
        phone = "123-456-7890",
        cell = "098-765-4321",
        idName = "SSN",
        idValue = "123-45-6789",
        picture = Picture(
            large = "large_pic_url",
            medium = "medium_pic_url",
            thumbnail = "thumbnail_pic_url"
        ),
        isFavorite = true
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        usersRepository = UsersRepositoryImpl(usersApi, usersDao)
    }

    @Test
    fun `fetchUsers calls API and saves data`() = runTest {
        // Arrange
        val query = 10
        val usersDto =
            UsersDto(
                info = mockk(relaxed = true),
                results = listOf(mockk(relaxed = true))
            )

        coEvery { usersApi.getUsers(query) } returns usersDto
        coEvery { usersDao.insertAll(any()) } returns Unit

        // Act
        usersRepository.fetchUsers(query)

        // Assert
        coVerify(exactly = 1) { usersApi.getUsers(query) }
        coVerify(exactly = 1) { usersDao.insertAll(any()) }
    }

    @Test
    fun `getUsersAsFlow returns all users`() = runTest {
        // Arrange
        val userEntities = listOf(userEntity)
        val userDomainModels = listOf(userEntity.toDomain())
        val userFlow: Flow<List<UserEntity>> = flow { emit(userEntities) }

        every { usersDao.getAllUsersAsFlow() } returns userFlow

        // Act
        val result = usersRepository.getUsersAsFlow().toList()

        // Assert
        assertEquals(listOf(userDomainModels), result)
        coVerify(exactly = 1) { usersDao.getAllUsersAsFlow() }
    }

    @Test
    fun `getFavoriteUsersAsFlow returns favorite users`() = runTest {
        // Arrange
        val favoriteUserEntities = listOf(userEntity)
        val favoriteUserDomainModels = listOf(userEntity.toDomain())
        val favoriteUserFlow: Flow<List<UserEntity>> = flow { emit(favoriteUserEntities) }

        every { usersDao.getFavoriteUsersAsFlow() } returns favoriteUserFlow

        // Act
        val result = usersRepository.getFavoriteUsersAsFlow().toList()

        // Assert
        assertEquals(listOf(favoriteUserDomainModels), result)
        coVerify(exactly = 1) { usersDao.getFavoriteUsersAsFlow() }
    }

    @Test
    fun `getUserByIdAsFlow returns user by ID`() = runTest {
        // Arrange
        val userId = 1L
        val userEntity = userEntity

        val userDomainModel = userEntity.toDomain()
        val userFlow: Flow<UserEntity> = flow { emit(userEntity) }

        every { usersDao.getUserByIdAsFlow(userId) } returns userFlow

        // Act
        val result = usersRepository.getUserByIdAsFlow(userId).toList()

        // Assert
        assertEquals(listOf(userDomainModel), result)
        coVerify(exactly = 1) { usersDao.getUserByIdAsFlow(userId) }
    }

    @Test
    fun `updateIsFavoriteById calls dao with correct parameters`() = runTest {
        // Arrange
        val userId = 1L
        val isFavorite = true
        coEvery { usersDao.updateIsFavoriteById(userId, isFavorite) } returns Unit

        // Act
        usersRepository.updateIsFavoriteById(userId, isFavorite)

        // Assert
        coVerify(exactly = 1) { usersDao.updateIsFavoriteById(userId, isFavorite) }
    }
}