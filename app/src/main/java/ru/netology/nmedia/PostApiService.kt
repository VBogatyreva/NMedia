package ru.netology.nmedia

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PostApiService {

    @GET("posts")
    suspend fun getAll(): Response<List<FeedFragment.Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<FeedFragment.Post>>

    @GET("posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<FeedFragment.Post>>

    @GET("posts/{id}/before")
    suspend fun getBefore(@Path("id") id: Long, @Query("count") count: Int): Response<List<FeedFragment.Post>>

    @GET("posts/{id}/after")
    suspend fun getAfter(@Path("id") id: Long, @Query("count") count: Int): Response<List<FeedFragment.Post>>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<FeedFragment.Post>

    @DELETE("posts/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Long): Response<FeedFragment.Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("posts")
    suspend fun save(@Body post: FeedFragment.Post): Response<FeedFragment.Post>

    @Multipart
    @POST("media")
    suspend fun uploadMedia(@Part file: MultipartBody.Part): Response<Media>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun authenticate(@Field("login") login: String, @Field("pass") password: String): Response<AuthModel>
}

object PostApi {

    fun sendTestPush(token: String, recipientId: Long?) {
        val client = OkHttpClient()

        val json = """
        {
            "recipientId": ${recipientId ?: "null"},
            "action": "NEW_POST",
            "content": "{\"userId\":1,\"userName\":\"Test User\",\"postId\":123,\"content\":\"Test content\"}"
        }
    """.trimIndent()

        val requestBody = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("http://localhost:9999/api/pushes?token=$token")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to send test push: ${e.message}")
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                println("Test push sent: ${response.code}")
            }
        })
    }
}