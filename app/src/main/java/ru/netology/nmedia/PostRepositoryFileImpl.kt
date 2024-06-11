package ru.netology.nmedia

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PostRepositoryFileImpl (private val context: Context) : PostRepository {

    private val gson = Gson()

    private val typeToken = TypeToken.getParameterized(List::class.java, MainActivity.Post::class.java).type
    private val filename = "posts.json"


    private var nextId = 1L
    private var posts = emptyList<MainActivity.Post>()


        private set(value) {
            field = value
            sync()
        }


    private val data = MutableLiveData(posts)

    init {

        val file = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, typeToken)
                nextId = posts.maxOfOrNull { it.id }?.inc() ?: 1
                data.value = posts
            }
        }

    }

    private fun sync() {
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
        }
    }

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

    override fun removeById(id: Long) {

        posts = posts.filter { it.id != id }
        data.value = posts

    }

    override fun edit(post:MainActivity.Post) {

        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }
       data.value = posts

    }

    override fun save(post: MainActivity.Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "now"
                )
            ) + posts
            data.value = posts

            return
        }
        posts = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)

        }
        data.value = posts

    }

    override fun video() {
        data.value = posts

    }


}