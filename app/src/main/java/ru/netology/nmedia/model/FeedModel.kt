package ru.netology.nmedia.model

import ru.netology.nmedia.ui.FeedFragment

data class FeedModel (

    val posts: List<FeedFragment.Post> = emptyList(),

    val empty: Boolean = false
)