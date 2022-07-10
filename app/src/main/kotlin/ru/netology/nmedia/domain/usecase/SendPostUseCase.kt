package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.params.NewPostParam

class SendPostUseCase(private val postRepository: PostRepository) {
    operator fun invoke(newPostParam: NewPostParam, callback: PostRepository.Callback<Post>)  {
        postRepository.send(newPostParam, callback)
    }
}