package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostViewModel(application: Application) : AndroidViewModel(application) {

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
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.GetAllCallback<List<FeedFragment.Post>> {
            override fun onSuccess(posts: List<FeedFragment.Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
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

        repository.likeByIdAsync(id, object : PostRepository.GetAllCallback<FeedFragment.Post> {
            override fun onSuccess(posts: FeedFragment.Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id != id) it
                            else posts
                        })
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun unlikeById(id: Long) {

        repository.unlikeByIdAsync(id, object : PostRepository.GetAllCallback<FeedFragment.Post> {
            override fun onSuccess(posts: FeedFragment.Post) {
                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .map {
                            if (it.id != id) it
                            else posts
                        })
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun removeById(id: Long) {
        val old = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(posts = _data.value?.posts.orEmpty()
                .filter { it.id != id }
            )
        )
        repository.removeByIdAsync(id, object : PostRepository.GetAllCallback<Any> {
            override fun onSuccess(posts: Any) {
            }
            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        })
    }


    fun edit(post: FeedFragment.Post) {
        edited.value?.let {
            edited.value = post
        }
    }


    fun save() {
        edited.value?.let {
            repository.saveAsync(it, object : PostRepository.GetAllCallback<FeedFragment.Post> {
                override fun onSuccess(posts: FeedFragment.Post) {
                    _postCreated.postValue(Unit)
                }
                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
        }
        edited.value = empty
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
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    shares = 0,
    visibility = 0,
    videoUrl = ""
)