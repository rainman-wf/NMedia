package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.common.constants.UNSENT_POST_ID_OFFSET
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.repository.UnsentPostRepository

class SavePostUseCase(
    private val postRepository: PostRepository,
    private val unsentPostRepository: UnsentPostRepository
) {
    operator fun invoke(id: Long = 0, content: String): Post {
        return when {
            id == 0L -> unsentPostRepository.save(id, content)
            id >= UNSENT_POST_ID_OFFSET ->
                unsentPostRepository.save(id - UNSENT_POST_ID_OFFSET, content)
            else -> postRepository.update(id, content)
        }
    }
}