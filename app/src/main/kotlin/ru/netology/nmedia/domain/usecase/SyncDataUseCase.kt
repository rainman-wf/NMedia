package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

class SyncDataUseCase @Inject constructor(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke() {
        return postRepository.syncData()
    }
}