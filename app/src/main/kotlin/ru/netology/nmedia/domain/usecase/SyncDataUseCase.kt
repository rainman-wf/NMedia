package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository

class SyncDataUseCase(private val postRepository: PostRepository) {
    suspend operator fun invoke() {
        postRepository.syncData()
    }
}