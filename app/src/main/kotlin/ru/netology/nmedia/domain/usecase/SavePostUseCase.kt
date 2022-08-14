package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.common.utils.log
import ru.netology.nmedia.domain.models.NewPostDto
import ru.netology.nmedia.domain.models.PostModel
import ru.netology.nmedia.domain.models.UpdatePostDto
import ru.netology.nmedia.domain.repository.PostRepository

class SavePostUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(key: Long, content: String) {
        if (key == 0L) postRepository.save(NewPostDto(content))
        else postRepository.update(UpdatePostDto(key, content))
    }
}
