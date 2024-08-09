package com.example.testapp.user.data

import com.example.testapp.user.data.local.UsersDao
import com.example.testapp.user.data.local.model.toDomain
import com.example.testapp.user.data.local.model.toEntity
import com.example.testapp.user.data.remote.UsersApi
import com.example.testapp.user.data.remote.model.UsersDto
import com.example.testapp.user.domain.model.UserDomainModel
import com.example.testapp.user.domain.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepositoryImpl @Inject constructor(
    private val usersApi: UsersApi,
    private val usersDao: UsersDao
) : UsersRepository {
    override suspend fun fetchUsers(query: Int) {
        val users = usersApi.getUsers(query)
        saveUsers(users)
    }

    private suspend fun saveUsers(users: UsersDto) {
        users.results.let { results ->
            usersDao.insertAll(users = results.map { it.toEntity() })
        }
    }

    override fun getUsersAsFlow(): Flow<List<UserDomainModel>> {
        return usersDao.getAllUsersAsFlow()
            .mapNotNull { userEntities -> userEntities.map { it.toDomain() } }
    }

    override fun getFavoriteUsersAsFlow(): Flow<List<UserDomainModel>> {
        return usersDao.getFavoriteUsersAsFlow()
            .mapNotNull { userEntities -> userEntities.map { it.toDomain() } }
    }

    override fun getUserByIdAsFlow(id: Long): Flow<UserDomainModel> {
        return usersDao.getUserByIdAsFlow(id).map { it.toDomain() }
    }

    override suspend fun updateIsFavoriteById(id: Long, isFavorite: Boolean) {
        usersDao.updateIsFavoriteById(id, isFavorite)
    }
}