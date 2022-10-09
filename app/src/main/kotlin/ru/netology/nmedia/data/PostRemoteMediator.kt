package ru.netology.nmedia.data

import androidx.paging.*
import androidx.room.withTransaction
import ru.netology.nmedia.common.exceptions.ApiError
import ru.netology.nmedia.common.exceptions.AppError
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.api.ApiService
import ru.netology.nmedia.data.local.AppDb
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.local.dao.PostRemoteKeyDao
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.local.entity.PostRemoteKeyEntity
import ru.netology.nmedia.data.mapper.toEntity
import java.io.IOException
import java.lang.NullPointerException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val appDb: AppDb
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            log(loadType)
            val response = when (loadType) {
                LoadType.REFRESH -> apiService.getLatest(state.config.initialLoadSize)
                LoadType.PREPEND -> {
                    val id = postRemoteKeyDao.max() ?: return MediatorResult.Success(false)
                    apiService.getAfter(id, state.config.pageSize)
                }
                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(false)
                    apiService.getBefore(id, state.config.pageSize)
                }
            }

            if (!response.isSuccessful) throw ApiError(response.code(), response.message())

            val body = response.body() ?: throw NullPointerException("body is null")


            appDb.withTransaction {

                when (loadType) {
                    LoadType.REFRESH -> {
                        postDao.clear()
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
                    }
                    LoadType.PREPEND -> {
                        postRemoteKeyDao.insert(
                            listOf(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.AFTER,
                                    body.first().id
                                )
                            )
                        )
                    }
                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            listOf(
                                PostRemoteKeyEntity(
                                    PostRemoteKeyEntity.KeyType.BEFORE,
                                    body.last().id
                                )
                            )
                        )
                    }
                }

                postDao.insert(body.map { it.toEntity() })
            }

            return MediatorResult.Success(body.isEmpty())

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}