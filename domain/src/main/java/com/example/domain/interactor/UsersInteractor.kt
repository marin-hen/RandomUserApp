package com.example.domain.interactor

import com.example.common.di.IoDispatcher
import com.example.domain.UsersRepository
import com.example.domain.model.UserDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersInteractor @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val usersRepository: UsersRepository
) {
    suspend fun fetchUsers(request: Int) =
        withContext(dispatcher) { usersRepository.fetchUsers(request) }

    fun getUsersAsFlow(isFavoriteUsersEnabled: Boolean): Flow<List<UserDomainModel>> {
        return if (isFavoriteUsersEnabled) {
            usersRepository.getFavoriteUsersAsFlow().flowOn(dispatcher)
        } else {
            usersRepository.getUsersAsFlow().flowOn(dispatcher)
        }
    }

    suspend fun updateIsFavoriteById(id: Long, isFavorite: Boolean) {
        withContext(dispatcher) { usersRepository.updateIsFavoriteById(id, isFavorite) }
    }
}