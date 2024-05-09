package com.task.xm.core.datasources.remote

import com.task.xm.data.model.AnswerEntity
import com.task.xm.data.model.QuestionEntity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * @Author Abdullah Abo El~Makarem on 07/05/2024.
 */
interface ApiService {
    @GET("/questions")
    fun fetchQuestions(): Call<List<QuestionEntity>>


    @POST("/question/submit")
    fun answerQuestion(@Body answerEntity: AnswerEntity): Call<ResponseBody>
}