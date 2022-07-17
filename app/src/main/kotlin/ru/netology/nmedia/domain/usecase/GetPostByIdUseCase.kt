package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.common.constants.UNSENT_POST_ID_OFFSET
import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.repository.UnsentPostRepository

class GetPostByIdUseCase(
    private val postRepository: PostRepository,
    private val unsentPostRepository: UnsentPostRepository
) {
    operator fun invoke(id: Long): PostModel {
        return if (id >= UNSENT_POST_ID_OFFSET)
            PostModel(
                unsentPostRepository.getById(id - UNSENT_POST_ID_OFFSET),
                statusError = true,
            )
        else PostModel(postRepository.getById(id))
    }
}

