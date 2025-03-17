package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface PostRepository {

    val data: LiveData<List<FeedFragment.Post>>

    suspend fun getAll()
    suspend fun likeById(id: Long)
    suspend fun unlikeById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(post: FeedFragment.Post)


    suspend fun getPostById(id: Long)
    suspend fun shareById(id:Long)
    suspend fun sawById(id:Long)
    suspend fun video()
}