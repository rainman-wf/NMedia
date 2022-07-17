package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository

class SyncDataUseCase(private val postRepository: PostRepository) {
    operator fun invoke(callback: PostRepository.Callback<Unit>) {
        postRepository.syncData(callback)
    }
}