package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository
import ru.netology.nmedia.domain.usecase.dto.NewPost

class AddNewPostUseCase(private val postRepository: PostRepository) {
    operator fun invoke(newPost: NewPost) = postRepository.addNew(newPost)
}