package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

interface PostRepository {
    fun get() : LiveData<MainActivity.Post>
    fun like()
    fun share()
    fun saw()
}

class PostRepositoryInMemoryImpl : PostRepository {

    private var post = MainActivity.Post(
        id = 1,
        author = "Нетология. Университет интренет-профессий будущего",
        published = "21 мая в 18:36",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
        likedByMe = false,
        likes = 0,
        sharedByMe = false,
        shares = 0,
        sawByMe = false,
        visibility = 0
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<MainActivity.Post> = data

    override fun like() {
        post = post.copy(likedByMe = !post.likedByMe)
        if (post.likedByMe) post.likes++ else post.likes--
        data.value = post
    }

    override fun share() {
        post = post.copy(sharedByMe = !post.sharedByMe)
        post.shares++
        data.value = post
    }

    override fun saw() {
        post = post.copy(sawByMe = !post.sawByMe)
        post.visibility++
        data.value = post
    }

class PostViewModel (private val repository : PostRepository = PostRepositoryInMemoryImpl()) : ViewModel() {

    val data = repository.get()
    fun like() = repository.like()
    fun share() = repository.share()
    fun saw() = repository.saw()

    }
}
