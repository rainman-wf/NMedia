package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository

class SetReadUseCase(private val repository: PostRepository) {
    suspend operator fun invoke(key: Long) {
        repository.setRead(key)
    }
}