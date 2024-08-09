package com.example.testapp.user.domain.usecase

import com.example.testapp.di.IoDispatcher
import com.example.testapp.user.domain.UsersRepository
import com.example.testapp.user.domain.model.UserDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UserDetailsUseCase @Inject constructor(
    @IoDispatcher val dispatcher: CoroutineDispatcher,
    private val usersRepository: UsersRepository
) {
    fun getUserByIdAsFlow(id: Long): Flow<UserDomainModel> {
        return usersRepository.getUserByIdAsFlow(id).flowOn(dispatcher)
    }
}