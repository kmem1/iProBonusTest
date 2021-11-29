package com.progressterra.api_module.common

sealed class RequestResult<T> {
    class Loading<T> : RequestResult<T>()
    data class Success<T>(val data: T) : RequestResult<T>()
    data class Failed<T>(val message: String) : RequestResult<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}