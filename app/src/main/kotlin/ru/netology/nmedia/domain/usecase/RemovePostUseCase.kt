package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository

class RemovePostUseCase(private val repository: PostRepository) {
    operator fun invoke(id: Long) {
        repository.remove(id)
    }
}