package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository

class GetAllUseCase(private val postRepository: PostRepository) {
    operator fun invoke(callback: PostRepository.Callback<List<Post>>) {
        return postRepository.getAll(callback)
    }
}