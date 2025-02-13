package ru.netology.nmedia

interface PostRepository {

    fun getAll() : List<FeedFragment.Post>
    fun likeById(id:Long)
    fun shareById(id:Long)
    fun sawById(id:Long)
    fun removeById(id:Long)
    fun save(post:FeedFragment.Post)
    fun video()
    fun getPostById(id: Long): FeedFragment.Post
}