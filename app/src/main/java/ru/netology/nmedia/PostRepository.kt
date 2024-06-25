package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface PostRepository {
    fun getAll() : LiveData<List<FeedFragment.Post>>
    fun likeById(id:Long)
    fun shareById(id:Long)
    fun sawById(id:Long)
    fun removeById(id:Long)
    fun save(post:FeedFragment.Post)
    fun edit(post:FeedFragment.Post)
    fun video()

}