package ru.netology.nmedia

import androidx.lifecycle.map
import androidx.room.Transaction
import okio.IOException

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {

    override val data = dao.getAll().map(List<PostEntity>::toDto)

    @Transaction
    override suspend fun getAll() {
        try {
            val response = PostApi.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    @Transaction
    override suspend fun likeById(id: Long) {
        try {
            dao.likeById(id)
            val response = PostApi.retrofitService.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

//            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            dao.likeById(id)
            throw NetworkError
        } catch (e: Exception) {
            dao.likeById(id)
            throw UnknownError
        }
    }

    @Transaction
    override suspend fun unlikeById(id: Long) {
        try {
            dao.likeById(id)
            val response = PostApi.retrofitService.unlikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
//            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            dao.likeById(id)
            throw NetworkError
        } catch (e: Exception) {
            dao.likeById(id)
            throw UnknownError
        }
    }



    @Transaction
    override suspend fun removeById(id: Long) {
        try {
            dao.markAsDeleted(id)
            val response = PostApi.retrofitService.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            dao.unmarkAsDeleted(id)
            throw NetworkError
        } catch (e: Exception) {
            dao.unmarkAsDeleted(id)
            throw UnknownError
        }
    }

    @Transaction
    override suspend fun save(post: FeedFragment.Post) {
        try {
            val postEntity = PostEntity.fromDto(post)
            dao.insert(postEntity)

            val response = PostApi.retrofitService.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
//            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            dao.insert(PostEntity.fromDto(body))

        } catch (e: IOException) {
            dao.removeById(post.id)
            throw NetworkError
        } catch (e: Exception) {
            dao.removeById(post.id)
            throw UnknownError
        }
    }

    override suspend fun getPostById(id: Long) {}
    override suspend fun shareById(id: Long) {}
    override suspend fun sawById(id: Long) {}
    override suspend fun video() {}
}
