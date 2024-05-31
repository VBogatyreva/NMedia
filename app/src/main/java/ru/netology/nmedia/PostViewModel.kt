package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel (private val repository : PostRepository = PostRepositoryInMemoryImpl()) : ViewModel() {

    val data = repository.getAll()
    val edited = MutableLiveData(empty)
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun sawById(id: Long) = repository.sawById(id)
    fun removeById(id:Long) = repository.removeById(id)
    fun edit(post : MainActivity.Post) {
        edited.value = post
    }

    fun cancel(content:String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun changeContent(content:String){
        edited.value?.let {
            val text = content.trim()
            if (it.content == text) {
                return
            }
            edited.value = it.copy(content = text)
        }
    }
}

private val empty = MainActivity.Post(
    id = 0,
    author = "",
    published = "",
    content = "",
    likedByMe = false,
    likes = 0,
    sharedByMe = false,
    shares = 0,
    sawByMe = false,
    visibility = 0
 )