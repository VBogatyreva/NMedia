package ru.netology.nmedia

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    private val prefs: SharedPreferences
) {
    private val _authStateFlow = MutableStateFlow<AuthState>(AuthState())
    val authStateFlow: StateFlow<AuthState> = _authStateFlow

    init {
        val id = prefs.getLong(PREF_KEY_ID, 0L)
        val token = prefs.getString(PREF_KEY_TOKEN, null)
        if (id != 0L && token != null) {
            _authStateFlow.value = AuthState(id, token)
        }
    }

    @Synchronized
    fun setAuth(id: Long, token: String) {
        _authStateFlow.value = AuthState(id, token)
        with(prefs.edit()) {
            putLong(PREF_KEY_ID, id)
            putString(PREF_KEY_TOKEN, token)
            apply()
        }
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        with(prefs.edit()) {
            clear()
            apply()
        }
    }

    data class AuthState(
        val id: Long = 0,
        val token: String? = null
    ) {
        val isAuthorized: Boolean
            get() = id != 0L && token != null
    }

    companion object {
        private const val PREF_KEY_ID = "id"
        private const val PREF_KEY_TOKEN = "token"
    }
}