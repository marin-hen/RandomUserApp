package com.example.domain

import com.example.domain.model.UserDomainModel
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    suspend fun fetchUsers(query: Int)
    suspend fun updateIsFavoriteById(id: Long, isFavorite: Boolean)
    fun getUsersAsFlow(): Flow<List<UserDomainModel>>
    fun getFavoriteUsersAsFlow(): Flow<List<UserDomainModel>>
    fun getUserByIdAsFlow(id: Long): Flow<UserDomainModel>
}