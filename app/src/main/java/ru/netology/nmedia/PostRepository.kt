package ru.netology.nmedia

interface PostRepository {

//    fun getAll() : List<FeedFragment.Post>
//    fun likeById(id:Long)
//    fun removeById(id:Long)
//    fun save(post:FeedFragment.Post)
//    fun getPostById(id: Long): FeedFragment.Post
    fun shareById(id:Long)
    fun sawById(id:Long)
    fun video()

    fun getPostById(id: Long, callback: GetAllCallback<FeedFragment.Post>)

    fun getAllAsync(callback: GetAllCallback<List<FeedFragment.Post>>)
    fun likeByIdAsync(id: Long, callback: GetAllCallback <FeedFragment.Post>)
    fun unlikeByIdAsync(id: Long, callback: GetAllCallback <FeedFragment.Post>)
    fun removeByIdAsync(id: Long, callback: GetAllCallback <Unit>)
    fun saveAsync(post: FeedFragment.Post, callback: GetAllCallback <FeedFragment.Post>)

    interface GetAllCallback<T> {
        fun onSuccess(posts:T) {}
        fun onError(e:Exception) {}
    }
}