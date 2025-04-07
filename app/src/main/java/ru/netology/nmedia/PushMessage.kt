package ru.netology.nmedia

data class PushMessage(
    val recipientId: Long?,
    val action: Action,
    val content: String
)