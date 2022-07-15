package ru.netology.nmedia.domain.usecase.container

import ru.netology.nmedia.domain.usecase.*

class PostListUseCaseContainer(
    val getAllUseCase: GetAllUseCase,
    val sendPostUseCase: SendPostUseCase,
    val likePostUseCase: LikePostUseCase,
    val removePostUseCase: RemovePostUseCase,
    val syncDataUseCase: SyncDataUseCase
)