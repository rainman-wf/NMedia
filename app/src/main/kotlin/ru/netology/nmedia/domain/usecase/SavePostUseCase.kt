package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.Attachment
import ru.netology.nmedia.domain.models.Author
import ru.netology.nmedia.domain.models.NewPostDto
import ru.netology.nmedia.domain.models.UpdatePostDto
import ru.netology.nmedia.domain.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavePostUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(key: Long, content: String, attachment: Attachment?) {
        if (key == 0L) postRepository.save(NewPostDto(Author(1,"name"), content, attachment))
        else postRepository.update(UpdatePostDto(key, content))
    }
}
