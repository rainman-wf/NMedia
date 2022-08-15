package ru.netology.nmedia.common.exceptions

import java.io.IOException
import java.sql.SQLException

sealed class AppError (var code: String) : RuntimeException() {
    companion object {
        fun from(e: Throwable) : AppError = when (e) {
            is ApiError -> e
            is SQLException -> DbError
            is IOException -> NetworkError
            else -> UnexpectedError
        }
    }
}

class ApiError(val status: Int, code: String) : AppError(code)
object NetworkError: AppError("network error")
object DbError: AppError("db error")
object UnexpectedError: AppError("unexpected error")