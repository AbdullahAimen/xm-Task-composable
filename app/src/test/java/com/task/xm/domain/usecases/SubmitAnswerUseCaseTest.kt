package com.task.xm.domain.usecases

import com.task.xm.core.datasources.Failure
import com.task.xm.core.datasources.Resource
import com.task.xm.data.model.QuestionEntity
import com.task.xm.domain.models.Question
import com.task.xm.domain.repositories.QuestionsRepository
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.internal.runners.statements.Fail
import org.mockito.internal.matchers.Not

/**
 * @Author Abdullah Abo El~Makarem on 09/05/2024.
 */
class SubmitAnswerUseCaseTest {
    private val questionsRepo: QuestionsRepository = mockk()

    private lateinit var submitAnswerUseCase: SubmitAnswerUseCase

    @Before
    fun setUp() {
        submitAnswerUseCase = SubmitAnswerUseCase(questionsRepo)
    }

    @Test
    fun `submitAnswer with correct answer returns success`() {
        // Given
        val successResult = Resource.success()
        coEvery { questionsRepo.submitAnswer(1, "Answer") } returns successResult

        // When
        val result = submitAnswerUseCase.invoke(1, "Answer")

        // Then
        Assert.assertEquals(result.isSuccess(), successResult.isSuccess())
    }


    @Test
    fun `submitAnswer with wrong answer returns failure`() {
        // Given
        val failureResult = Resource.failure<Nothing>(Failure())
        coEvery { questionsRepo.submitAnswer(1, "Answer") } returns failureResult

        // When
        val result = submitAnswerUseCase.invoke(1, "Answer")

        // Then
        Assert.assertEquals(result.isFailure(), failureResult.isFailure())
    }
}