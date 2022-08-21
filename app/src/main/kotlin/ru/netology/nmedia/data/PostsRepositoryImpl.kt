package ru.netology.nmedia.data


import androidx.core.net.toFile
import androidx.core.net.toUri
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.constants.BASE_URL
import ru.netology.nmedia.common.exceptions.ApiError
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.api.ApiService
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.mapper.toEntity
import ru.netology.nmedia.data.mapper.toModel
import ru.netology.nmedia.data.mapper.toRequestBody
import ru.netology.nmedia.domain.models.*
import ru.netology.nmedia.domain.repository.PostRepository

class PostsRepositoryImpl(
    private val api: ApiService,
    private val postDao: PostDao
) : PostRepository {

    override val posts: Flow<List<PostModel>> =
        postDao.getAll().map { it.map(PostEntity::toModel) }.flowOn(Dispatchers.Default)

    override suspend fun getByKey(key: Long) = postDao.getByKey(key).toModel()

    override suspend fun save(newPostDto: NewPostDto) {
        val key = postDao.insert(newPostDto.toEntity())
        sendPost(key)
    }

    override suspend fun update(updatePostDto: UpdatePostDto) {
        postDao.updateContentByKey(updatePostDto.key, updatePostDto.content)
        sendPost(updatePostDto.key)
    }

    override suspend fun sendPost(key: Long) {

        val entity = postDao.getByKey(key)
        if (entity.id == 0L) postDao.setState(key, PostModel.State.LOADING)

        if (entity.attachment != null && !entity.attachment.url.contains("http")) {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    log("try upload image")
                    upload(UploadMediaDto(entity.attachment.url.toUri().toFile()))?.id
                }?.let { postDao.setMediaUrl(key, "${BASE_URL}/media/$it") }
            }
        }

        try {
            log("try send post")
            val post = api.send(entity.toRequestBody()).body()!!
            postDao.setServerId(key, post.id)
            postDao.setSyncedById(post.id)
            if (entity.id == 0L) postDao.setServerDateTime(key, post.published)
            postDao.setState(key, PostModel.State.OK)
        } catch (e: Exception) {
            postDao.setState(key, PostModel.State.ERROR)
        }
    }

    override suspend fun like(key: Long) {
        val id = postDao.likeByKey(key)
        val liked = postDao.getByKey(key).likedByMe
        if (liked) sendLike(id).toModel(key) else sendUnlike(id).toModel(key)
    }

    private suspend fun sendLike(id: Long): Post {
        val response = api.like(id)
        if (!response.isSuccessful) error("Response code: ${response.code()}")
        val post = response.body() ?: error("Body is null")
        postDao.setSyncedById(id)
        return post
    }

    private suspend fun sendUnlike(id: Long): Post {
        val response = api.unlike(id)
        if (!response.isSuccessful) error("Response code: ${response.code()}")
        val post = response.body() ?: error("Body is null")
        postDao.setSyncedById(id)
        return post
    }

    override suspend fun remove(key: Long) {
        val entity = postDao.setRemovedByKey(key)
        if (entity.id == 0L) {
            postDao.removeByKey(key)
            return
        }
        tryRemove(entity.id)
    }

    private suspend fun tryRemove(id: Long) {
        val response = api.remove(id)
        if (!response.isSuccessful) error("Response code: ${response.code()}")
        postDao.removeById(id)
    }

    private suspend fun getAllRemote(): List<Post> {
        val response = api.getAll()
        if (!response.isSuccessful) error("Response code: ${response.code()}")
        return response.body() ?: error("Body is null")
    }

    override suspend fun syncData() {

        val posts = getAllRemote() // посты с сервера

        postDao.getAllRemovedIds().forEach { tryRemove(it) }

        postDao.getAllSentIds().sorted()
            .filterNot(posts.map { it.id }::contains)
            .forEach { postDao.removeById(it) }

        posts
            .filter { postDao.getAllSentIds().contains(it.id) }
            .forEach { it.sync(postDao.getById(it.id)) }

        postDao.getAllUnsent().forEach { sendPost(it.key) }
        /* возможно лучше оставить это решение за пользователем
        после первой же неудачной попытке отправить */
    }

    override suspend fun setRead(key: Long) {
        postDao.setRead(key)
    }

    private fun Post.sameAsEntity(entity: PostEntity): Boolean {
        return content == entity.content &&
                likedByMe == entity.likedByMe &&
                likes == entity.likes
    }

    private suspend fun Post.sync(entity: PostEntity) {
        if (!entity.synced) {
            if (author == AUTHOR && content != content) sendPost(entity.key)
            if (likedByMe) sendLike(id) else sendUnlike(id)
        } else {
            if (!sameAsEntity(entity)) {
                postDao.replace(entity)
            }
        }
    }

    override fun getNewerCount() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(10000)
                val last = postDao.getAllSentIds().maxOrNull() ?: 0L
                try {
                    val response = api.getNewer(last)
                    val body =
                        response.body() ?: throw ApiError(response.code(), response.message())
                    postDao.insert(body.map { it.toEntity(0, true, PostModel.State.OK, false) })
                } catch (e: Exception) {
                    log(e.message.toString())
                }
            }
        }
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
            log(e.message.toString())
            null
        }
    }
}