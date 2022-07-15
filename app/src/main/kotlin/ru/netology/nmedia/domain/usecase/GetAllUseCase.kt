package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.repository.UnsentPostRepository

class GetAllUseCase(
    private val postRepository: PostRepository,
    private val unsentPostRepository: UnsentPostRepository
) {
    operator fun invoke(): List<PostModel> {
        val list = mutableListOf<PostModel>()
        list.addAll(postRepository.getAll().map { PostModel(it) })
        list.addAll(unsentPostRepository.getAll().map { PostModel(it, statusError = true) })
        return list.sortedBy { it.post.dateTime }.reversed()
    }
}