package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class PostViewModel (application: Application) : AndroidViewModel(application) {

    private val repository : PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    private var draft = ""
    fun saveDraft(text: String) {
        draft = text
    }
    fun dropDraft() {
        draft = ""
    }
    fun getDraft() = draft

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun sawById(id: Long) = repository.sawById(id)
    fun removeById(id:Long) = repository.removeById(id)
    fun video() = repository.video()
    fun edit(post : FeedFragment.Post) {
        edited.value?.let {
            edited.value = post
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

private val empty = FeedFragment.Post(
    id = 0,
    author = "",
    published = "",
    content = "",
    likedByMe = false,
    likes = 0,
    shares = 0,
    visibility = 0,
    videoUrl = ""
)