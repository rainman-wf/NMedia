package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.dto.UpdatePostContent

class UpdatePostContentUseCase(private val postRepository: PostRepository) {

    operator fun invoke(updatePostContent: UpdatePostContent) {
        postRepository.updateContent(updatePostContent)
    }
}