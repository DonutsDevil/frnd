package com.swapnil.frnd.utility

class BaseConstants {
    companion object {
        sealed class Status<T>(val data: T? = null, val errorMsg: String? = null) {
            class Loading<T> : Status<T>()
            class Success<T>(data: T) : Status<T>(data = data)
            class Error<T>(errorMsg: String) : Status<T>(errorMsg = errorMsg)
        }
    }

    enum class Error(val msg: String) {
        NO_INTERNET("Please try again with internet connection")
    }
}