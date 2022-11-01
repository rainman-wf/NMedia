package ru.netology.nmedia.data

import androidx.paging.*
import androidx.room.withTransaction
import ru.netology.nmedia.common.exceptions.ApiError
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.api.ApiService
import ru.netology.nmedia.data.api.dto.PostResponseBody
import ru.netology.nmedia.data.local.AppDb
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.local.dao.PostRemoteKeyDao
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.local.entity.PostRemoteKeyEntity
import ru.netology.nmedia.data.mapper.toEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val appDb: AppDb,
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        log(loadType)
        try {
            val response = when (loadType) {
                LoadType.REFRESH -> {
                    postRemoteKeyDao.max()?.let {
                        apiService.getAfter(it, state.config.pageSize)
                    } ?: apiService.getLatest(state.config.initialLoadSize)
                }
                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getBefore(id, state.config.pageSize)
                }
                LoadType.PREPEND -> {
                    val id = postRemoteKeyDao.max() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getAfter(id, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) return MediatorResult.Error(
                ApiError(
                    response.code(),
                    response.message(),
                )
            )

            val body = response.body() ?: return MediatorResult.Error(
                ApiError(
                    response.code(),
                    response.message(),
                )
            )

            if (body.isEmpty()) return MediatorResult.Success(true)

            appDb.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        if (postRemoteKeyDao.isEmpty())
                            postRemoteKeyDao.insert(
                                listOf(
                                    PostRemoteKeyEntity(
                                        PostRemoteKeyEntity.KeyType.AFTER,
                                        body.first().id
                                    ),
                                    PostRemoteKeyEntity(
                                        PostRemoteKeyEntity.KeyType.BEFORE,
                                        body.last().id
                                    ),
                                )
                            )
                        else
                            postRemoteKeyDao.insert(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.AFTER,
                                    body.first().id
                                )
                            )
                    }
                    LoadType.PREPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                type = PostRemoteKeyEntity.KeyType.AFTER,
                                key = body.first().id,
                            )
                        )
                    }
                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                type = PostRemoteKeyEntity.KeyType.BEFORE,
                                key = body.last().id,
                            )
                        )
                    }
                }
                postDao.insert(body.map(PostResponseBody::toEntity))
            }

            return MediatorResult.Success(body.isEmpty())

        } catch (e: Exception) {
            log("ERROR : ${e::class.simpleName} : ${e.message}")
            return MediatorResult.Error(e)
        }
    }
}