package com.task.xm.domain.usecases

import com.task.xm.core.datasources.Failure
import com.task.xm.core.datasources.Resource
import com.task.xm.data.model.QuestionEntity
import com.task.xm.domain.models.Question
import com.task.xm.domain.repositories.QuestionsRepository
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.internal.runners.statements.Fail

/**
 * @Author Abdullah Abo El~Makarem on 09/05/2024.
 */
class QuestionUseCaseTest {

    private val questionsRepo: QuestionsRepository = mockk()
    private lateinit var questionUseCase: QuestionUseCase

    @Before
    fun setUp() {
        questionUseCase = QuestionUseCase(questionsRepo)
    }


    @Test
    fun `fetchQuestions success`() {
        // Given

        val questions = listOf(
            QuestionEntity(
                id = 1, question = "first question", answer = "Answer"
            )
        )
        val successResult = Resource.success(questions)
        coEvery { questionsRepo.fetchQuestions() } returns successResult

        // When
        val result = questionUseCase.invoke()

        // Then
        assertEquals(result.isSuccess(), successResult.isSuccess())
        assertEquals(result.requireSuccessValue(), successResult.requireSuccessValue().map { Question(it.id, it.question, it.answer, false) })
    }

    @Test
    fun `fetchQuestions failure`() {
        // Given
        val failureResult = Resource.failure<List<QuestionEntity>>(Failure())
        coEvery { questionsRepo.fetchQuestions() } returns failureResult

        // When
        val result = questionUseCase.invoke()

        // Then
        assertEquals(result.isFailure(), failureResult.isFailure())
        assertEquals(result.failureValue().exception, failureResult.failureValue().exception)
    }

}