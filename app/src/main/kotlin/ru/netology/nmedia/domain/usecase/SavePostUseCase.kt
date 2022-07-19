package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.models.UpdatePostDto
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.repository.UnsentPostRepository

class SavePostUseCase(
    private val postRepository: PostRepository,
    private val unsentPostRepository: UnsentPostRepository
) {
    operator fun invoke(id: Long = 0, content: String): Post {
        return if (id <= 0L) unsentPostRepository.save(id, content)
        else postRepository.update(UpdatePostDto(id, content))
    }
}
