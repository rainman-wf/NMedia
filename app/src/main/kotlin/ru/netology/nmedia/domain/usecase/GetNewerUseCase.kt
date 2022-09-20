package ru.netology.nmedia.domain.usecase

import ru.netology.nmedia.domain.repository.PostRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNewerUseCase @Inject constructor(
    private val repository: PostRepository
) {

    operator fun invoke() {
        return repository.getNewerCount()
    }
}