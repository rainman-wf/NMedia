package ru.netology.nmedia.domain.usecase.container

import ru.netology.nmedia.domain.usecase.*

class NewPostUseCaseContainer(
    val trySendPostUseCase: TrySendPostUseCase,
    val savePostUseCase: SavePostUseCase,
    val removePostUseCase: RemovePostUseCase
)
