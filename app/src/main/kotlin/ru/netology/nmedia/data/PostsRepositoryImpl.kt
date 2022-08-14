package ru.netology.nmedia.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.common.constants.AUTHOR
import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.data.api.ApiService
import ru.netology.nmedia.data.local.dao.PostDao
import ru.netology.nmedia.data.local.entity.PostEntity
import ru.netology.nmedia.data.mapper.toEntity
import ru.netology.nmedia.data.mapper.toModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.data.mapper.toRequestBody
import ru.netology.nmedia.domain.models.NewPostDto
import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.domain.models.UpdatePostDto

class PostsRepositoryImpl(
    private val api: ApiService,
    private val postDao: PostDao
) : PostRepository {

    override val posts: LiveData<List<PostModel>> =
        postDao.getAll().map { it.map(PostEntity::toModel) }

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

        log(postDao.getByKey(key).id)

        val entity = postDao.getByKey(key)
        if (entity.id == 0L) postDao.setState(key, PostModel.State.LOADING)

        log(postDao.getByKey(key).state)

        try {
            val post = api.send(entity.toRequestBody()).body()!!
            postDao.setServerId(key, post.id)
            postDao.setSyncedById(post.id)
            postDao.setState(key, PostModel.State.OK)
        } catch (_: Exception) {
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

    private suspend fun getAllRemote(): Map<Long, Post> {
        val response = api.getAll()
        if (!response.isSuccessful) error("Response code: ${response.code()}")
        return response.body()?.associateBy { it.id } ?: error("Body is null")
    }

    override suspend fun syncData() {

        val posts = getAllRemote()

        val sentPost = postDao.getAllSent()
        val sentPostIds = sentPost.map { it.id }

        val postsToInsert = posts.values
            .filterNot { sentPostIds.contains(it.id) }

        postDao.insert(postsToInsert.map {
            it.toEntity(0, true, PostModel.State.OK)
        })

        postDao.getAllRemovedIds().forEach { tryRemove(it) }

        sentPostIds
            .filterNot(posts.values.map { it.id }::contains)
            .forEach { postDao.removeById(it) }

        sentPost.forEach { entity -> posts[entity.id]?.let { it -> entity.sync(it) } }
        postDao.getAllUnsent().forEach { sendPost(it.key) }
    }

    private fun PostEntity.sameAsPost(post: Post): Boolean {
        return content == post.content &&
                likedByMe == post.likedByMe &&
                likes == post.likes
    }

    private suspend fun PostEntity.sync(post: Post) {
        if (!synced) {
            if (author == AUTHOR && content != post.content) sendPost(this.key)
            if (likedByMe) sendLike(id) else sendUnlike(id)
            if (removed) tryRemove(id)
        } else {
            if (!this.sameAsPost(post)) {
                postDao.replace(this)
            }
        }
    }
}