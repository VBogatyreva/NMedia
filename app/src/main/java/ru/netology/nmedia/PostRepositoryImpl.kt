package ru.netology.nmedia

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.PostRepository.GetAllCallback

class PostRepositoryImpl : PostRepository {

    override fun getAllAsync(callback: GetAllCallback<List<FeedFragment.Post>>) {
        PostApi.retrofitService.getAll()
            .enqueue(object : Callback<List<FeedFragment.Post>> {
                override fun onResponse(
                    call: Call<List<FeedFragment.Post>>,
                    response: Response<List<FeedFragment.Post>>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: run {
                        callback.onError(
                            RuntimeException(
                                response.message() + response.code().toString()
                            )
                        )
                        return
                    })
                }

                override fun onFailure(call: Call<List<FeedFragment.Post>>, e: Throwable) {
                    callback.onError(RuntimeException(e))
                }
            })
    }

    override fun likeByIdAsync(id: Long, callback: GetAllCallback<FeedFragment.Post>) {
        PostApi.retrofitService.likeById(id)
            .enqueue(object : Callback<FeedFragment.Post> {
                override fun onResponse(
                    call: Call<FeedFragment.Post>,
                    response: Response<FeedFragment.Post>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: run {
                        callback.onError(
                            RuntimeException(
                                response.message() + response.code().toString()
                            )
                        )
                        return
                    })
                }

                override fun onFailure(call: Call<FeedFragment.Post>, e: Throwable) {
                    callback.onError(RuntimeException(e))
                }
            })
    }

    override fun unlikeByIdAsync(id: Long, callback: GetAllCallback<FeedFragment.Post>) {
        PostApi.retrofitService.unlikeById(id)
            .enqueue(object : Callback<FeedFragment.Post> {
                override fun onResponse(
                    call: Call<FeedFragment.Post>,
                    response: Response<FeedFragment.Post>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: run {
                        callback.onError(
                            RuntimeException(
                                response.message() + response.code().toString()
                            )
                        )
                        return
                    })
                }

                override fun onFailure(call: Call<FeedFragment.Post>, e: Throwable) {
                    callback.onError(RuntimeException(e))
                }
            })
    }

    override fun saveAsync(post: FeedFragment.Post, callback: GetAllCallback<FeedFragment.Post>){
        PostApi.retrofitService.save(post)
            .enqueue(object : Callback<FeedFragment.Post> {
                override fun onResponse(
                    call: Call<FeedFragment.Post>,
                    response: Response<FeedFragment.Post>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: run {
                        callback.onError(
                            RuntimeException(
                                response.message() + response.code().toString()
                            )
                        )
                        return
                    })
                }

                override fun onFailure(call: Call<FeedFragment.Post>, e: Throwable) {
                    callback.onError(RuntimeException(e))
                }
            })
    }

    override fun removeByIdAsync(id: Long, callback: GetAllCallback<Unit>) {
        PostApi.retrofitService.removeById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: run {
                        callback.onError(
                            RuntimeException(
                                response.message() + response.code().toString()
                            )
                        )
                        return
                    })
                }

                override fun onFailure(call: Call<Unit>, e: Throwable) {
                    callback.onError(RuntimeException(e))
                }
            })
    }

    override fun getPostById (id: Long, callback: GetAllCallback<FeedFragment.Post>) {
        PostApi.retrofitService.getPostById(id)
            .enqueue(object : Callback<FeedFragment.Post> {
                override fun onResponse(
                    call: Call<FeedFragment.Post>,
                    response: Response<FeedFragment.Post>
                ) {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    callback.onSuccess(response.body() ?: run {
                        callback.onError(
                            RuntimeException(
                                response.message() + response.code().toString()
                            )
                        )
                        return
                    })
                }

                override fun onFailure(call: Call<FeedFragment.Post>, e: Throwable) {
                    callback.onError(RuntimeException(e))
                }
            })
    }

    override fun shareById(id: Long) { }
    override fun sawById(id: Long) { }
    override fun video() { }
}
