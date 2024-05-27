package ru.netology.nmedia

import androidx.lifecycle.ViewModel

class PostViewModel (private val repository : PostRepository = PostRepositoryInMemoryImpl()) : ViewModel() {

    val data = repository.getAll()
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun sawById(id: Long) = repository.sawById(id)

}