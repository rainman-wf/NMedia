package ru.netology.nmedia.domain.usecase.params

data class UpdateCurrentPostParam(
    val id: Long,
    val content: String
)