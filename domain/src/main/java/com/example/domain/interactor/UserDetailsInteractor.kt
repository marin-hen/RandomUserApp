package com.example.domain.interactor

import com.example.common.di.IoDispatcher
import com.example.domain.UsersRepository
import com.example.domain.model.UserDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserDetailsInteractor @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val usersRepository: UsersRepository
) {
    fun getUserByIdAsFlow(id: Long): Flow<UserDomainModel> {
        return usersRepository.getUserByIdAsFlow(id).flowOn(dispatcher)
    }
}