package ru.netology.nmedia.domain.usecase.interactor

import ru.netology.nmedia.domain.usecase.*

class NewPostInteractor(
    val sendPostUseCase: SendPostUseCase,
    val getAllWorkpiecesUseCase: GetAllWorkpiecesUseCase,
    val getAllUseCase: GetAllUseCase,
    val saveWorkpieceUseCase: SaveWorkpieceUseCase,
    val removeWorkpieceUseCase: RemoveWorkpieceUseCase
)
