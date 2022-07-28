package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.NewPostDto
import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository

class SendPostUseCase(private val postRepository: PostRepository) {
    operator fun invoke(content: String, callback: PostRepository.Callback<Post>)  {
        postRepository.send(NewPostDto(content = content), callback)
    }
}