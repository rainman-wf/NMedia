package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository

class AddIncomingPostUseCase (private val postRepository: PostRepository) {
    operator fun invoke(post: Post) = postRepository.addIncoming(post)
}