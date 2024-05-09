package com.task.xm.data.repository

import com.task.xm.core.datasources.Resource
import com.task.xm.core.datasources.remote.XmRemoteDataSource
import com.task.xm.data.model.AnswerEntity
import com.task.xm.data.model.QuestionEntity
import com.task.xm.domain.repositories.QuestionsRepository
import javax.inject.Inject

/**
 * @Author Abdullah Abo El~Makarem on 08/05/2024.
 */
open class QuestionsRepositoryImpl @Inject constructor(private val xmRemoteDataSource: XmRemoteDataSource) :
    QuestionsRepository {
    override fun fetchQuestions(): Resource<List<QuestionEntity>> =
        xmRemoteDataSource.fetchQuestions()

    override fun submitAnswer(id: Int, answer: String): Resource<Nothing> =
        xmRemoteDataSource.submitAnswer(AnswerEntity(id, answer))
}