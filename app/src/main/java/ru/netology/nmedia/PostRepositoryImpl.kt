package ru.netology.nmedia

import androidx.lifecycle.map

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {

    override fun getAll() = dao.getAll().map {list ->
        list.map{
            FeedFragment.Post(
                it.id,
                it.author,
                it.published,
                it.content,
                it.likedByMe,
                it.likes,
                it.shares,
                it.visibility,
                it.videoUrl
            )
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun sawById(id: Long) {
        dao.sawById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: FeedFragment.Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun video() {

    }
}