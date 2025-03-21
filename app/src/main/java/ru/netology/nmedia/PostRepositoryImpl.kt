package ru.netology.nmedia

import androidx.lifecycle.map
import okio.IOException

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {

    override val data = dao.getAll().map(List<PostEntity>::toDto)

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

    override suspend fun likeById(id: Long) {
        try {
            dao.likeById(id)
            val response = PostApi.retrofitService.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            dao.likeById(id)
            throw NetworkError
        } catch (e: Exception) {
            dao.likeById(id)
            throw UnknownError
        }
    }

    override suspend fun unlikeById(id: Long) {
        try {
            dao.likeById(id)
            val response = PostApi.retrofitService.unlikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            //ответ сервера не используем
//            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            // откатываем изменения в случае ошибки сети
            dao.likeById(id)
            throw NetworkError
        } catch (e: Exception) {
            // откатываем изменения в случае других ошибок
            dao.likeById(id)
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        try {
             //метка удаления, чтобы ее потом снять в случае ошибки
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


    override suspend fun save(post: FeedFragment.Post) {
        try {
            val response = PostApi.retrofitService.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getPostById(id: Long) {}
    override suspend fun shareById(id: Long) {}
    override suspend fun sawById(id: Long) {}
    override suspend fun video() {}
}
