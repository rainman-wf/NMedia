package ru.netology.nmedia.domain.models

data class PostModel(
    val key: Long,
    val post: Post,
    val state: State = State.LOADING
) {
    enum class State {
        LOADING,
        ERROR,
        OK
    }
}