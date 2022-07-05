package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.data.network.NetworkService
import ru.netology.nmedia.data.network.dto.PostResponse
import ru.netology.nmedia.data.network.mapper.toModel
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.dto.NewPost
import ru.netology.nmedia.domain.usecase.dto.UpdatePostContent

class PostsRemoteDataRepositoryImpl(private val networkService: NetworkService): PostRepository {

    override fun addNew(newPost: NewPost): Post {
        return networkService.save(
            PostResponse(
                author = newPost.author,
                content = newPost.content
            )
        ).toModel()
    }

    override fun updateContent(updatePostContent: UpdatePostContent): Post {
        return networkService.save(
            PostResponse(
                id = updatePostContent.id,
                content = updatePostContent.content
            )
        ).toModel()
    }

    override fun getAll(): List<Post> {
        return networkService.getAll().map { it.toModel() }
    }

    override fun like(id: Long): Post {
        networkService.apply {
            val isLiked = getByID(id).likedByMe
            return  if (!isLiked) likeById(id).toModel() else unlikeById(id).toModel()
        }
    }

    override fun share(id: Long): Post {
        TODO("Not yet implemented")
    }

    override fun remove(id: Long): Boolean {
        networkService.removeById(id)
        return true
    }

    override fun getById(id: Long): Post {
        return networkService.getByID(id).toModel()
    }

    override fun getObservableById(id: Long): LiveData<Post> {
        TODO("Not yet implemented")
    }

    override fun addIncoming(post: Post): Long {
        TODO("Not yet implemented")
    }
}