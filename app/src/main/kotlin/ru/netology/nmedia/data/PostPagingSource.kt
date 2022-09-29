package ru.netology.nmedia.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.netology.nmedia.common.exceptions.NetworkError
import ru.netology.nmedia.data.api.ApiService
import ru.netology.nmedia.data.api.dto.PostResponseBody
import java.io.IOException
import javax.inject.Inject

class PostPagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Long, PostResponseBody>() {

    override fun getRefreshKey(state: PagingState<Long, PostResponseBody>): Long? = null

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, PostResponseBody> {
        try {
            val result = when (params) {
                is LoadParams.Refresh -> {
                    apiService.getLatest(params.loadSize)
                }
                is LoadParams.Append -> {
                    apiService.getBefore(
                        id = params.key,
                        count = params.loadSize
                    )
                }
                is LoadParams.Prepend -> return LoadResult.Page(
                    data = emptyList(),
                    nextKey = null,
                    prevKey = params.key
                )
            }
            if (!result.isSuccessful) throw NetworkError

            val data = result.body().orEmpty()
            return LoadResult.Page(
                data = data,
                prevKey = params.key,
                nextKey = data.lastOrNull()?.id
            )
        } catch (e: IOException) {
            return LoadResult.Error(e)
        }
    }
}