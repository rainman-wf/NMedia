package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.domain.repository.PostRepository

class GetPostByIdUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(key: Long): PostModel {
        return postRepository.getByKey(key)
    }
}

