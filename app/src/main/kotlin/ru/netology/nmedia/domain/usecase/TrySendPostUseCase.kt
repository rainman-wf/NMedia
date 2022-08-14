package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository

class TrySendPostUseCase(private val postRepository: PostRepository) {
    suspend operator fun invoke(key: Long) {
        postRepository.sendPost(key)
    }
}