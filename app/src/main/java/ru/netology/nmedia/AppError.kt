package ru.netology.nmedia

sealed class AppError(private val code: String): RuntimeException()

class ApiError(val status: Int, code: String): AppError(code)
object NetworkError : AppError(code = "Error_NO_Network")
object UnknownError: AppError(code = "Error_Unknown")