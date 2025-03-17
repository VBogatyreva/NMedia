package ru.netology.nmedia

data class FeedModel (

    val posts: List<FeedFragment.Post> = emptyList(),

    val empty: Boolean = false,
    val loading: Boolean = false,
    val error: Boolean = false,
    val messageCodeError: String = ""
)