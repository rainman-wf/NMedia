package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository

class GetNewerUseCase(private val repository: PostRepository) {

    operator fun invoke() {
        return repository.getNewerCount()
    }
}