package ru.netology.nmedia.services

enum class Action {
    LIKE, NEW_POST, ERROR;

    companion object {
        fun getValidAction(action: String): Action {
            return try {
                valueOf(action)
            } catch (exception: IllegalArgumentException) {
                ERROR
            }
        }
    }
}