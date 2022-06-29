package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import ru.netology.nmedia.data.db.dao.PostDao
import ru.netology.nmedia.data.db.entity.PostEntity
import ru.netology.nmedia.data.db.mapper.toEntity
import ru.netology.nmedia.data.db.mapper.toModel
import ru.netology.nmedia.data.youtubeApi.RetrofitInstance
import ru.netology.nmedia.domain.models.FirstUrl
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.dto.NewPost
import ru.netology.nmedia.domain.usecase.dto.UpdatePostContent

class PostRepositoryImpl(private val postDao: PostDao) : PostRepository {

    override fun addNew(newPost: NewPost): Post {

        val postEntity = PostEntity(
            author = newPost.author,
            content = newPost.content,
            firstUrl = getFirstUrl(newPost.url)
        )
        val postId = postDao.insert(postEntity)
        return postDao.getById(postId).toModel()
    }

    override fun updateContent(updatePostContent: UpdatePostContent): Boolean {

        val postEntity = postDao.getById(updatePostContent.id).copy(
            content = updatePostContent.content,
            firstUrl = getFirstUrl(updatePostContent.url)
        )
        return postDao.update(postEntity) == 1
    }

    override fun getAll(): LiveData<List<Post>> =
        postDao.getAll().map { entities -> entities.map { it.toModel() } }

    override fun like(id: Long): Boolean {
        return postDao.like(id) == 1
    }

    override fun share(id: Long): Boolean {
        return postDao.share(id) == 1
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

    private fun getFirstUrl(url: String?): FirstUrl? {
        if (url == null) return null

        var firstUrl: FirstUrl? = null

        val load = Thread {
            url.let {
                val apiResponse = RetrofitInstance.service.getData(it).execute()
                firstUrl = if (apiResponse.code() == 200) FirstUrl(it, apiResponse.body()) else null
            }
        }

        load.start()
        load.join(5 * 1000)

        return firstUrl
    }
}