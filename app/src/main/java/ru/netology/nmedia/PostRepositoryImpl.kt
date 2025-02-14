package ru.netology.nmedia

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<FeedFragment.Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(): List<FeedFragment.Post> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()
        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }

    override fun getAllAsync(callback: PostRepository.GetAllCallback<List<FeedFragment.Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun likeById(id: Long) {
        val request: Request = Request.Builder()
            .post(gson.toJson(id).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()
        client.newCall(request)
            .execute()
            .close()
    }

    override fun likeByIdAsync(
        id: Long,
        callback: PostRepository.GetAllCallback<FeedFragment.Post>
    ) {
        val request: Request = Request.Builder()
            .post(gson.toJson(id).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts/${id}/likes")
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, FeedFragment.Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun shareById(id: Long) {
    }

    override fun sawById(id: Long) {
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()
        client.newCall(request)
            .execute()
            .close()
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.GetAllCallback<Any>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(body)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun save(post: FeedFragment.Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()
        client.newCall(request)
            .execute()
            .close()
    }

    override fun saveAsync(
        post: FeedFragment.Post,
        callback: PostRepository.GetAllCallback<FeedFragment.Post>
    ) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(body, FeedFragment.Post::class.java))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun video() {}

    override fun getPostById(id: Long): FeedFragment.Post {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()
        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson(it, typeToken.type)
            }
    }
}