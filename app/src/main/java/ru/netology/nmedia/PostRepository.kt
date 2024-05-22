package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface PostRepository {
    fun get() : LiveData<MainActivity.Post>
    fun like()
    fun share()
    fun saw()
}