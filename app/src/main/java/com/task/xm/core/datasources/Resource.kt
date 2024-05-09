package com.task.xm.core.datasources

import com.task.xm.core.datasources.remote.ApiErrorException


/**
 * @Author Abdullah Abo El~Makarem on 07/05/2024.
 */
class Resource<T> private constructor(
        private val successValue: T? = null,
        private val failureValue: Failure? = null
) {

    fun isSuccess(): Boolean = failureValue == null

    fun isFailure(): Boolean = !isSuccess()

    fun successValue(): T? = successValue

    fun requireSuccessValue(): T = successValue!!

    fun failureValue(): Failure = failureValue!!

    fun onSuccess(action: (successValue: T?) -> Unit): Resource<T> {
        if (isSuccess()) { action.invoke(successValue) }
        return this
    }

    fun onFailure(action: (failureValue: Failure?) -> Unit): Resource<T> {
        if (!isSuccess()) { action.invoke(failureValue) }
        return this
    }

    fun onSuccessWithValue(action: (successValue: T) -> Unit): Resource<T> {
        if (isSuccess()) successValue?.let { action.invoke(it) }
        return this
    }

    fun onFailureWithValue(action: (failureValue: Failure) -> Unit): Resource<T> {
        if (!isSuccess()) failureValue?.let { action.invoke(it) }
        return this
    }

    companion object {

        fun success(): Resource<Nothing> = Resource()

        fun <T> success(value: T): Resource<T> = Resource(successValue = value)

        fun <T> failure(failure: Failure): Resource<T> = Resource(failureValue = failure)
    }
}

data class Failure(
    val message: String? = null,
    val exception: Exception = ApiErrorException()
)

fun <T> tryCatch(
    block: () -> Resource<T>
): Resource<T> = try {
    block()
} catch (e: Exception) {
    Resource.failure(Failure(exception = e))
}