package ru.netology.nmedia.services.dto

data class Notify(
    val content: String,
    val recipientId: Long?
)