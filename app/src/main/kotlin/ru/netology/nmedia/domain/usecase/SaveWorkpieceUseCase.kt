package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.UnsentPostRepository
import ru.netology.nmedia.domain.usecase.params.NewPostParam

class SaveWorkpieceUseCase(private val repository: UnsentPostRepository) {
    operator fun invoke (newPostParam: NewPostParam) : Post {
        return repository.save(newPostParam)
    }
}