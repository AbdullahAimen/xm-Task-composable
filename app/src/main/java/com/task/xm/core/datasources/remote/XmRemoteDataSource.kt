package com.task.xm.core.datasources.remote

import com.task.xm.core.datasources.Failure
import com.task.xm.core.datasources.Resource
import com.task.xm.data.model.AnswerEntity
import retrofit2.HttpException
import javax.inject.Inject

/**
 * @Author Abdullah Abo El~Makarem on 07/05/2024.
 */
open class XmRemoteDataSource @Inject constructor(private val apiService: ApiService) {

    fun fetchQuestions() = tryCatch {
        val response = apiService.fetchQuestions().execute().body()!!
        Resource.success(response)
    }

    fun submitAnswer(answer: AnswerEntity) = tryCatch {
        val response = apiService.answerQuestion(answer).execute()
        if (response.code() == 200) {
            Resource.success()
        } else {
            Resource.failure(Failure(exception = ApiErrorException()))
        }

    }

    private fun <T> tryCatch(block: () -> Resource<T>): Resource<T> = try {
        block()
    } catch (exception: ApiErrorException) {
        Resource.failure(mapApiError(exception))
    } catch (httpException: HttpException) {
        Resource.failure(Failure(exception = httpException))
    } catch (e: Exception) {
        Resource.failure(Failure(exception = e))
    }

    private fun mapApiError(exception: ApiErrorException): Failure {
        return Failure(exception = exception)
    }
}