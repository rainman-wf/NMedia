package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.common.constants.UNSENT_POST_ID_OFFSET
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.repository.UnsentPostRepository

class RemovePostUseCase(
    private val postRepository: PostRepository,
    private val unsentPostRepository: UnsentPostRepository
) {
    operator fun invoke(id: Long) {
        if (id >= UNSENT_POST_ID_OFFSET)
            unsentPostRepository.remove(id - UNSENT_POST_ID_OFFSET)
        else postRepository.remove(id)
    }
}