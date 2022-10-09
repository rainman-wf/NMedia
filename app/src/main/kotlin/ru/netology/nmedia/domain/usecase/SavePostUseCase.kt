package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.Attachment
import ru.netology.nmedia.domain.models.NewPostDto
import ru.netology.nmedia.domain.models.UpdatePostDto
import ru.netology.nmedia.domain.repository.PostRepository
import javax.inject.Inject

class SavePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(id: Long, content: String, attachment: Attachment?) {
        if (id == 0L) postRepository.sendPost(NewPostDto(content = content, attachment = attachment))
        else postRepository.update(UpdatePostDto(id, content))
    }
}
