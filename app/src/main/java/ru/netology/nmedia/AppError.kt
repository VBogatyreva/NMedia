package ru.netology.nmedia

import android.database.SQLException
import java.io.IOException

sealed class AppError(var code: String) : RuntimeException() {
    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is IOException -> NetworkError
            else -> UnknownError
        }
    }
}

class ApiError(val status: Int, code: String) : AppError(code)
object NetworkError : AppError("Error_NO_Network")
object DbError : AppError("Error_db")
object UnknownError : AppError("Error_Unknown")
