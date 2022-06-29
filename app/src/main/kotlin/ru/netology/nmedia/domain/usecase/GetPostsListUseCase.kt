package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository

class GetPostsListUseCase(private val postRepository: PostRepository) {
    operator fun invoke () = postRepository.getAll()
}