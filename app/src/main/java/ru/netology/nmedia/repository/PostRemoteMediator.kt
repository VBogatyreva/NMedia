package ru.netology.nmedia.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.netology.nmedia.error.ApiError
import ru.netology.nmedia.api.PostApiService
import ru.netology.nmedia.entity.PostEntity
import ru.netology.nmedia.entity.PostRemoteKeyEntity
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dao.PostRemoteKeyDao
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.entity.toEntity

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val postApiService: PostApiService,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val appDb: AppDb,
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    val firstItemId = postDao.getFirstId()
                    firstItemId?.let { id ->
                        postApiService.getAfter(id, state.config.pageSize)
                    } ?: postApiService.getLatest(state.config.initialLoadSize)
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    postApiService.getBefore(id, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            val newPosts = body.toEntity()

            appDb.withTransaction {
                if (loadType == LoadType.REFRESH && postRemoteKeyDao.isEmpty()) {
                    postDao.clear()
                    postRemoteKeyDao.clear()
                }
                val existingIds = postDao.getAllIds().toSet()
                val postsToInsert = newPosts.filter { it.id !in existingIds }

                if (postsToInsert.isNotEmpty()) {
                    postDao.insert(postsToInsert)

                    when (loadType) {
                        LoadType.REFRESH -> {
                            if (newPosts.isNotEmpty()) {
                                postRemoteKeyDao.insert(
                                    PostRemoteKeyEntity(
                                        type = PostRemoteKeyEntity.KeyType.AFTER,
                                        key = newPosts.first().id,
                                    )
                                )

                                if (postRemoteKeyDao.isEmpty()) {
                                    postRemoteKeyDao.insert(
                                        PostRemoteKeyEntity(
                                            type = PostRemoteKeyEntity.KeyType.BEFORE,
                                            key = newPosts.last().id,
                                        )
                                    )
                                }
                            }
                        }
                        LoadType.APPEND -> {
                            postRemoteKeyDao.insert(
                                PostRemoteKeyEntity(
                                    type = PostRemoteKeyEntity.KeyType.BEFORE,
                                    key = newPosts.last().id,
                                )
                            )
                        }
                        else -> {}
                    }
                }
            }

            return MediatorResult.Success(endOfPaginationReached = newPosts.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}






