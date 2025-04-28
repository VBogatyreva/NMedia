package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: PostRepository,
    private val appAuth: AppAuth
) : ViewModel() {
    private val _authState = MutableLiveData<AuthFragment.AuthState>()
    val authState: LiveData<AuthFragment.AuthState> = _authState

    fun authenticate(login: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthFragment.AuthState.Loading
            try {
                val authModel = repository.authenticate(login, password)
                appAuth.setAuth(authModel.id, authModel.token)
                _authState.value = AuthFragment.AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthFragment.AuthState.Error(e.message ?: "Authentication error")
            }
        }
    }
}



