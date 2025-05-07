package ru.netology.nmedia.dto

import ru.netology.nmedia.service.Action

data class PushMessage(

    val recipientId: Long?,
    val action: Action,
    val content: String
)