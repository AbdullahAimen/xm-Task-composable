package com.task.xm.domain.usecases

import com.task.xm.core.datasources.Resource
import com.task.xm.domain.models.Question
import com.task.xm.domain.repositories.QuestionsRepository
import javax.inject.Inject

/**
 * @Author Abdullah Abo El~Makarem on 08/05/2024.
 */
class QuestionUseCase @Inject constructor(private val questionsRepo: QuestionsRepository) {

    operator fun invoke(): Resource<List<Question>> {

        val questionsResult = questionsRepo.fetchQuestions()
        if (questionsResult.isFailure()) {
            return Resource.failure(questionsResult.failureValue())
        }

        return Resource.success(
            questionsResult.successValue()?.map { Question(it.id, it.question, it.answer) }
                .orEmpty()
        )
    }
}