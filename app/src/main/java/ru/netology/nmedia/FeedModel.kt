package ru.netology.nmedia

data class FeedModel (

    val posts: List<FeedFragment.Post> = emptyList(),
    val loading: Boolean = false,
    val error: Boolean = false,
    val empty: Boolean = false
)