package com.android.project.lightweight.util

data class Resource<out T>(val status: Status, val data: T?, val message: String?, val query: String?) {
    companion object {
        fun <T> success(data: T?, query: String): Resource<T> {
            return Resource(Status.SUCCESS, data, null, query)
        }

        fun <T> error(msg: String, data: T?, query: String): Resource<T> {
            return Resource(Status.ERROR, data, msg, query)
        }

        fun <T> loading(data: T?, query: String): Resource<T> {
            return Resource(Status.LOADING, data, null, query)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}