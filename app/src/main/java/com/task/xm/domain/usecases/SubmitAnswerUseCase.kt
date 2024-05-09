package com.task.xm.domain.usecases

import com.task.xm.core.datasources.Resource
import com.task.xm.domain.repositories.QuestionsRepository
import javax.inject.Inject

/**
 * @Author Abdullah Abo El~Makarem on 08/05/2024.
 */
class SubmitAnswerUseCase @Inject constructor(private val questionsRepo: QuestionsRepository) {

    operator fun invoke(id: Int, answer: String): Resource<Nothing> {

        val questionsResult = questionsRepo.submitAnswer(id, answer)
        if (questionsResult.isFailure()) {
            return Resource.failure(questionsResult.failureValue())
        }

        return Resource.success()
    }
}