package com.tomasrepcik.blumodify.app.model

sealed class AppResult<T> {
    data class Success<T>(val data: T) : AppResult<T>()
    data class Error<T>(
        val message: String,
        val origin: String,
        val errorCause: ErrorCause,
        val stacktrace: Any? = null
    ) : AppResult<T>()

    override fun toString(): String = when (this) {
        is Error -> "Message: $message\n\nOrigin: $origin\n" +
                "\nError cause: $errorCause\n\nError:\n$stacktrace"
        is Success -> data.toString()
    }
}

enum class ErrorCause {
    WORKER_NOT_FOUND,
    MISSING_SETTINGS,
    MISSING_ID
}