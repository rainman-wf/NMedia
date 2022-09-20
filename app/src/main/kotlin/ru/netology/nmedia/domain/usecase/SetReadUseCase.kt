package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetReadUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(key: Long) {
        repository.setRead(key)
    }
}