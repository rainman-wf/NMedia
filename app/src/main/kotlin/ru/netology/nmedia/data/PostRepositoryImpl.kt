package ru.netology.nmedia.data

import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.common.exceptions.ApiError
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.api.ApiService
import ru.netology.nmedia.data.api.dto.PostRequestBody
import ru.netology.nmedia.data.local.AppDb
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.local.dao.PostRemoteKeyDao
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.mapper.toPost
import ru.netology.nmedia.domain.models.*
import ru.netology.nmedia.domain.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val postDao: PostDao,
    postRemoteKeyDao: PostRemoteKeyDao,
    appDb: AppDb
) : PostRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val posts: Flow<PagingData<FeedItem>> =
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { postDao.getPagingSource() },
            remoteMediator = PostRemoteMediator(
                apiService = api,
                postDao = postDao,
                postRemoteKeyDao = postRemoteKeyDao,
                appDb = appDb
            )
        ).flow.map {
            it.map(PostEntity::toPost)
                .insertSeparators { previous, _ ->
                    if (previous?.id?.rem(5) == 0L) {
                        Ad(Random.nextLong(), "figma.jpg")
                    } else {
                        null
                    }
                }
        }

    override suspend fun sendPost(newPostDto: NewPostDto) {

        val mediaId =
            newPostDto.attachment?.url?.toUri()?.let { UploadMediaDto(it.toFile()) }
                ?.let { upload(it)?.id }

        try {
            api.send(
                PostRequestBody(
                    content = newPostDto.content,
                    attachment = mediaId?.let { Attachment(url = it, Attachment.Type.IMAGE) }
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun update(updatePostDto: UpdatePostDto) {
        try {
            api.send(
                PostRequestBody(
                    id = updatePostDto.id,
                    content = updatePostDto.content,
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override suspend fun like(id: Long) {
        postDao.like(id)
        val entity = postDao.getById(id)

        val response =
            if (entity.likedByMe) {
                api.like(id)
            } else {
                api.unlike(id)
            }

        if (!response.isSuccessful) throw  ApiError(response.code(), response.message())
    }

    override suspend fun remove(id: Long) {
        val response = api.remove(id)
        if (!response.isSuccessful) throw  ApiError(response.code(), response.message())
        postDao.removeById(id)
    }

    private suspend fun upload(uploadMediaDto: UploadMediaDto): Media? {
        val media = MultipartBody.Part.createFormData(
            "file",
            uploadMediaDto.file.name,
            uploadMediaDto.file.asRequestBody()
        )
        return try {
            val response = api.uploadMedia(media)
            if (response.isSuccessful) response.body()
            else null
        } catch (e: Exception) {
            null
        }
    }
}