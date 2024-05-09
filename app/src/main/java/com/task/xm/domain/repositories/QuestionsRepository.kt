package com.task.xm.domain.repositories

import com.task.xm.core.datasources.Resource
import com.task.xm.data.model.QuestionEntity

/**
 * @Author Abdullah Abo El~Makarem on 08/05/2024.
 */

interface QuestionsRepository {
    fun fetchQuestions(): Resource<List<QuestionEntity>>
    fun submitAnswer(id:Int, answer: String): Resource<Nothing>
}