package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okio.IOException
import kotlin.concurrent.thread

class PostViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl()

    val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        thread {
            _data.postValue(FeedModel(loading = true))
            try {
                val posts = repository.getAll()
                FeedModel(posts = posts, empty = posts.isEmpty())
            } catch (e: IOException) {
                FeedModel(error = true)
            }.also(_data::postValue)
        }
    }


    private var draft = ""
    fun saveDraft(text: String) {
        draft = text
    }

    fun dropDraft() {
        draft = ""
    }

    fun getDraft() = draft


    fun shareById(id: Long) = repository.shareById(id)
    fun sawById(id: Long) = repository.sawById(id)
    fun video() = repository.video()

    fun likeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .map { post ->
                        if (post.id == id) post.copy(
                            likedByMe = !post.likedByMe,
                            likes = post.likes + if (post.likedByMe) -1 else 1
                        )
                        else post
                    }
                )
            )
            try {
                repository.likeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }

    }

    fun removeById(id: Long) {
        thread {
            val old = _data.value?.posts.orEmpty()
            _data.postValue(
                _data.value?.copy(posts = _data.value?.posts.orEmpty()
                    .filter { it.id != id }
                )
            )
            try {
                repository.removeById(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        }
    }


    fun edit(post: FeedFragment.Post) {
        edited.value?.let {
            edited.value = post
        }
    }


    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
            }
            edited.value = empty
        }
    }

    fun changeContent(content: String) {
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