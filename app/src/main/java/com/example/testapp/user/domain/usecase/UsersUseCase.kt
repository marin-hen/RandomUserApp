package com.example.testapp.user.domain.usecase

import com.example.testapp.di.IoDispatcher
import com.example.testapp.user.domain.UsersRepository
import com.example.testapp.user.domain.model.UserDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UsersUseCase @Inject constructor(
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