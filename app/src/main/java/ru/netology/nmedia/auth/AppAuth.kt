package ru.netology.nmedia.auth

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    private val authHolder: AuthHolder,
    private val repository: PostRepository,
    private val apiService: PostApiService
) {
    init {
        authHolder.authStateFlow
            .onEach { authState ->
                try {
                    if (authState.isAuthorized) {
                        repository.refresh()
                    } else {
                        repository.clear()
                    }
                } catch (e: Exception) {

                }
            }
            .launchIn(CoroutineScope(Dispatchers.Default))
    }

    suspend fun authenticate(login: String, password: String) {
        val response = apiService.authenticate(login, password)
        if (!response.isSuccessful) {
            throw ApiError(response.code(), response.message())
        }
        val body = response.body() ?: throw ApiError(response.code(), response.message())
        authHolder.setAuth(body.id, body.token)
    }

    fun logout() {
        authHolder.removeAuth()
    }

    fun setAuth(id: Long, token: String) {
        authHolder.setAuth(id, token)
    }
    val authStateFlow: StateFlow<AuthHolder.AuthState>
        get() = authHolder.authStateFlow

}