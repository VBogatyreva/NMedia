package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {


    private var posts = listOf(
        MainActivity.Post(
            id = 1,
            author = "Нетология. Университет интренет-профессий будущего",
            published = "21 мая в 18:36",
            content = "ПОСТ2 - Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
            likedByMe = false,
            likes = 0,
            sharedByMe = false,
            shares = 0,
            sawByMe = false,
            visibility = 0
        ),
        MainActivity.Post(
            id = 2,
            author = "Нетология. Университет интренет-профессий будущего",
            published = "22 мая в 18:36",
            content = "ПОСТ2 - Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать на путь роста и начать цепочку перемен - http://netolo.gy/fyb",
            likedByMe = false,
            likes = 0,
            sharedByMe = false,
            shares = 0,
            sawByMe = false,
            visibility = 0
        )
    )

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<MainActivity.Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {

            if (it.id != id) it else it.copy(likedByMe = !it.likedByMe,

            likes = if (!it.likedByMe) it.likes + 1 else it.likes - 1)
        }
        data.value = posts
    }

    override fun shareById(id: Long) {

        posts = posts.map {

            if (it.id != id) it else it.copy(shares = it.shares + 1)

        }
        data.value = posts
    }

    override fun sawById(id: Long) {

        posts = posts.map {

            if (it.id != id) it else it.copy(visibility = it.visibility + 1)

        }
        data.value = posts

    }
}