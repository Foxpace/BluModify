package com.tomasrepcik.blumodify.app.model

sealed class AppResult<T>{
    class Success<T>(val data: T): AppResult<T>()
    class Error<T>(val message: String, val errorCause: ErrorCause, val error: Any): AppResult<T>()
}

enum class ErrorCause {
    WORKER_NOT_FOUND
}