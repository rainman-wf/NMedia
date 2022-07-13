package ru.netology.nmedia.domain.usecase.interactor

import ru.netology.nmedia.domain.usecase.*

class NewPostInteractor(
    val sendPostUseCase: SendPostUseCase,
    val savePostUseCase: SavePostUseCase,
    val removePostUseCase: RemovePostUseCase
)
