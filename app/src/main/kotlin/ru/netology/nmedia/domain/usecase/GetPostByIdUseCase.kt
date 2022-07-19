package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.repository.UnsentPostRepository

class GetPostByIdUseCase(
    private val postRepository: PostRepository,
    private val unsentPostRepository: UnsentPostRepository
) {
    operator fun invoke(id: Long): PostModel {
        return if (id < 0)
            PostModel(
                unsentPostRepository.getById(id),
                statusError = true,
            )
        else PostModel(postRepository.getById(id))
    }
}

