package ru.netology.nmedia

import kotlinx.coroutines.flow.Flow

interface PostRepository {

    val data: Flow<List<FeedFragment.Post>>

    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun showAll()
    suspend fun likeById(id: Long)
    suspend fun unlikeById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(post: FeedFragment.Post)

    suspend fun saveWithAttachment(post: FeedFragment.Post, upload: MediaUpload)
    suspend fun uploadMedia(upload: MediaUpload): Media

    suspend fun authenticate(login: String, password: String): AuthModel

    suspend fun getPostById(id: Long?): FeedFragment.Post?
    suspend fun shareById(id:Long)
    suspend fun sawById(id:Long)
    suspend fun video()
}