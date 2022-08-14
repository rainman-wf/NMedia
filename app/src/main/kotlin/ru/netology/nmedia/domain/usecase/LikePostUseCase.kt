package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository

class LikePostUseCase(private val postRepository: PostRepository) {
    suspend operator fun invoke (key: Long)  {
        postRepository.like(key)
    }
}