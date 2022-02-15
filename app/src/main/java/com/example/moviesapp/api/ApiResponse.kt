package com.example.moviesapp.api

import retrofit2.Call
import retrofit2.Response

sealed class ApiResponse<out T> {

    class Success<T>(response: Response<T>) : ApiResponse<T>() {
        val data = response.body()
    }


    class Failure<T>(response: Response<T>) : ApiResponse<T>() {
        val message: String = response.errorBody().toString()
    }


    class Exception<T>(val exception: Throwable) : ApiResponse<T>()
}

fun <T> ApiResponse<T>.onSuccess(onResult: ApiResponse.Success<T>.() -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Success) onResult(this)
    return this
}

fun <T> ApiResponse<T>.onFailure(onResult: ApiResponse.Failure<*>.() -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Failure<*>)
        onResult(this)
    return this
}

fun <T> ApiResponse<T>.onException(onResult: ApiResponse.Exception<*>.() -> Unit): ApiResponse<T> {
    if (this is ApiResponse.Exception<*>) onResult(this)
    return this
}

inline fun <T> Call<T>.request(crossinline onResult: (response: ApiResponse<T>) -> Unit) {
    enqueue(object : retrofit2.Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                onResult(ApiResponse.Success(response))
            } else {
                onResult(ApiResponse.Failure(response))
            }
        }

        override fun onFailure(call: Call<T>, throwable: Throwable) {
            onResult(ApiResponse.Exception(throwable))
        }
    })
}