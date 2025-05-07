package ru.netology.nmedia.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.auth.AuthModel
import ru.netology.nmedia.ui.FeedFragment
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.MediaUpload

interface PostRepository {

    val data: Flow<PagingData<FeedFragment.Post>>
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun showAll()
    suspend fun likeById(id: Long)
    suspend fun unlikeById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(post: FeedFragment.Post)
    suspend fun refresh()
    suspend fun clear()

    suspend fun saveWithAttachment(post: FeedFragment.Post, upload: MediaUpload)
    suspend fun uploadMedia(upload: MediaUpload): Media

    suspend fun authenticate(login: String, password: String): AuthModel

    suspend fun getPostById(id: Long?): FeedFragment.Post?
    suspend fun shareById(id:Long)
    suspend fun sawById(id:Long)
    suspend fun video()
}




