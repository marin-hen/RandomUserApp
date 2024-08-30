package com.example.testapp.user.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.testapp.common.network.asResult
import com.example.testapp.user.domain.interactor.UserDetailsInteractor
import com.example.testapp.user.presentation.model.UserDetailsUiState
import com.example.testapp.user.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.example.testapp.common.network.Result
import com.example.testapp.user.navigation.Screen
import kotlinx.coroutines.flow.flowOf

@HiltViewModel
internal class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userDetailsInteractor: UserDetailsInteractor
) : BaseViewModel() {

    private val _state: Flow<UserDetailsUiState> =
        savedStateHandle.toRoute<Screen.UserDetails>().userId?.let {
            userDetailsInteractor.getUserByIdAsFlow(it)
                .map { user -> UserDetailsUiState(user = user.toUiModel()) }
                .asResult()
                .map { result ->
                    when (result) {
                        is Result.Loading -> UserDetailsUiState(isLoading = true)
                        is Result.Success -> result.data
                        is Result.Error -> UserDetailsUiState(errorMessage = result.exception?.message)
                    }
                }
        } ?: flowOf(UserDetailsUiState(errorMessage = "User ID is missing"))

    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserDetailsUiState(isLoading = true)
    )
}