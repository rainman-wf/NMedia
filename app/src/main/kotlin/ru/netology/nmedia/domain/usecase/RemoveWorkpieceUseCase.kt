package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.UnsentPostRepository

class RemoveWorkpieceUseCase(private val repository: UnsentPostRepository) {
    operator fun invoke(id: Long) : Int {
        return repository.remove(id)
    }
}