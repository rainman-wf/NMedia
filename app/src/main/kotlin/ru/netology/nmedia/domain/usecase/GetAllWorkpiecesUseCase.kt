package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.models.Post
import ru.netology.nmedia.domain.repository.UnsentPostRepository

class GetAllWorkpiecesUseCase(private val unsentPostRepository: UnsentPostRepository) {
    operator fun invoke() : List<Post> {
        return unsentPostRepository.getAllUnsent()
    }
}