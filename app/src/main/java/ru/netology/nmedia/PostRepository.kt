package ru.netology.nmedia

import androidx.lifecycle.LiveData

interface PostRepository {
    fun getAll() : LiveData<List<MainActivity.Post>>
    fun likeById(id:Long)
    fun shareById(id:Long)
    fun sawById(id:Long)
    fun removeById(id:Long)
    fun save(post:MainActivity.Post)
    fun edit(post:MainActivity.Post)


}