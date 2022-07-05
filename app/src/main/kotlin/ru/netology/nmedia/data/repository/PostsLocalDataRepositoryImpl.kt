package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.data.db.dao.PostDao
import ru.netology.nmedia.data.db.entity.FirstUrlEntity
import ru.netology.nmedia.data.db.entity.PostEntity
import ru.netology.nmedia.data.db.mapper.toEntity
import ru.netology.nmedia.data.db.mapper.toModel
import ru.netology.nmedia.data.youtubeApi.RetrofitInstance
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.dto.NewPost
import ru.netology.nmedia.domain.usecase.dto.UpdatePostContent

class PostsLocalDataRepositoryImpl(private val postDao: PostDao) : PostRepository {

    override fun addNew(newPost: NewPost): Post {

        val postEntity = PostEntity(
            author = newPost.author,
            content = newPost.content,
            firstUrl = getFirstUrl(newPost.url)
        )
        val postId = postDao.insert(postEntity)
        return postDao.getById(postId).toModel()
    }

    override fun updateContent(updatePostContent: UpdatePostContent): Post {
        return postDao.getById(updatePostContent.id).copy(
            content = updatePostContent.content,
            firstUrl = getFirstUrl(updatePostContent.url)
        ).toModel()
    }

    override fun getAll(): List<Post> =
        postDao.getAll().value?.toList()?.map { it.toModel() } ?: emptyList()

    override fun like(id: Long): Post {
        postDao.like(id)
        return postDao.getById(id).toModel()
    }

    override fun share(id: Long): Post {
        postDao.like(id)
        return postDao.getById(id).toModel()
    }

    override fun remove(id: Long): Boolean {
        return postDao.remove(id) == 1
    }

    override fun getById(id: Long): Post {
        return postDao.getById(id).toModel()
    }

    override fun getObservableById(id: Long): LiveData<Post> {
        return postDao.getObservableById(id).map { it.toModel() }
    }

    override fun addIncoming(post: Post): Long {
        return postDao.insert(post.toEntity())
    }

    private fun getFirstUrl(url: String?): FirstUrlEntity? {
        if (url == null) return null

        var firstUrl: FirstUrlEntity? = null

        val load = Thread {
            url.let {
                val apiResponse = RetrofitInstance.service.getThumbDataEntity(it).execute()
                firstUrl = if (apiResponse.code() == 200) FirstUrlEntity(it, apiResponse.body()) else null
            }
        }

        load.start()
        load.join(5 * 1000)

        return firstUrl
    }
}