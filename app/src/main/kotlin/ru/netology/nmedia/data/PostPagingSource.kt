package ru.netology.nmedia.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.netology.nmedia.data.api.ApiService
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.local.entity.PostEntity
import java.io.IOException
import javax.inject.Inject

class PostPagingSource @Inject constructor(
    private val apiService: ApiService,
    private val postDao: PostDao
) : PagingSource<Long, PostEntity>() {

    override fun getRefreshKey(state: PagingState<Long, PostEntity>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, PostEntity> {
        try {
            val result = when (params) {
                is LoadParams.Refresh -> {
                    postDao.getLatest(params.loadSize)
                }
                is LoadParams.Append -> {
                    postDao.getBefore(
                        key = params.key,
                        count = params.loadSize
                    )
                }
                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(),
                    nextKey = null,
                    prevKey = params.key
                )
            }

            return LoadResult.Page(
                data = result,
                prevKey = params.key,
                nextKey = result.lastOrNull()?.id
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}