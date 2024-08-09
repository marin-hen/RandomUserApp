package com.example.testapp.user.presentation.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.asResult
import com.example.testapp.user.domain.usecase.UserDetailsUseCase
import com.example.testapp.user.presentation.model.UserDetailsUiState
import com.example.testapp.user.presentation.model.toUiModel
import com.example.testapp.user.route.UserDetailsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.example.testapp.common.Result
import kotlinx.coroutines.flow.flowOf

@HiltViewModel
internal class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userDetailsUseCase: UserDetailsUseCase
) : BaseViewModel() {

    private val userId: Long? = savedStateHandle.get<String>(UserDetailsRoute.USER_ID)?.toLong()

    private val _state: Flow<UserDetailsUiState> = userId?.let {
        userDetailsUseCase.getUserByIdAsFlow(it)
            .map { user ->
                UserDetailsUiState.UserUiState(user = user.toUiModel())
            }.asResult()
            .map { result ->
                when (result) {
                    is Result.Loading -> UserDetailsUiState.LoadingUiState
                    is Result.Success -> result.data
                    is Result.Error -> UserDetailsUiState.ErrorUiState(result.exception?.message)
                }
            }
    } ?: flowOf(UserDetailsUiState.ErrorUiState("User ID is missing"))

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserDetailsUiState.LoadingUiState
    )
}