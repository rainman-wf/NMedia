package ru.netology.nmedia.domain.usecase.interactor

import ru.netology.nmedia.domain.usecase.*

class PostListInteractor(
    val getAllWorkpiecesUseCase: GetAllWorkpiecesUseCase,
    val getAllUseCase: GetAllUseCase,
    val sendPostUseCase: SendPostUseCase,
    val likePostUseCase: LikePostUseCase,
    val removePostUseCase: RemovePostUseCase
)