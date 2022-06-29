package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository

class LikePostUseCase(private val postRepository: PostRepository) {

    operator fun invoke (id: Long) {
        postRepository.like(id)
    }
}