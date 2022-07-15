package ru.netology.nmedia.domain.usecase.container

import ru.netology.nmedia.domain.usecase.GetPostByIdUseCase
import ru.netology.nmedia.domain.usecase.LikePostUseCase
import ru.netology.nmedia.domain.usecase.RemovePostUseCase

class PostDetailsUseCaseContainer (
    val likePostUseCase: LikePostUseCase,
    val removePostUseCase: RemovePostUseCase,
    val getPostByIdUseCase: GetPostByIdUseCase
)