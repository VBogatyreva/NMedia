package ru.netology.nmedia

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.File

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    val data: LiveData<FeedModel> = repository.data
        .map(::FeedModel)
        .asLiveData(Dispatchers.Default)

    private val noPhoto = PhotoModel()

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _currentPost = SingleLiveEvent<FeedFragment.Post?>()
    val currentPost:  LiveData<FeedFragment.Post?>
        get() = _currentPost

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel?>
        get() = _photo

    val newerCount: LiveData<Int> = data.switchMap {
        repository.getNewerCount(it.posts.firstOrNull()?.id ?: 0L)
            .catch { e -> e.printStackTrace() }
            .asLiveData(Dispatchers.Default)
    }

    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun getPostById(id: Long?){
        viewModelScope.launch {
            try {
                val result = repository.getPostById(id)
                _currentPost.value = result
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun loadVisiblePosts() = viewModelScope.launch {
        try {
            repository.showAll()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
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

    suspend fun shareById(id: Long) = repository.shareById(id)
    suspend fun sawById(id: Long) = repository.sawById(id)
    suspend fun video() = repository.video()

    fun likeById(id: Long) = viewModelScope.launch {
        try {
            repository.likeById(id)

        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun unlikeById(id: Long)  = viewModelScope.launch {
        try {
            repository.unlikeById(id)

        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            repository.removeById(id)

        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }


    fun edit(post: FeedFragment.Post) {
        edited.value?.let {
            edited.value = post
        }
    }

    fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    when (_photo.value) {
                        noPhoto -> repository.save(it)
                        else -> _photo.value?.file?.let { file ->
                            repository.saveWithAttachment(it, MediaUpload(file))
                        }
                    }
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
                _postCreated.postValue(Unit)
            }
            edited.postValue(empty)
            _photo.value = noPhoto
        }
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
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

    fun clearSelectedPost() {
        _currentPost.value = null
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
    videoUrl = "",
    hiddenPosts = true,
    attachment = null
)