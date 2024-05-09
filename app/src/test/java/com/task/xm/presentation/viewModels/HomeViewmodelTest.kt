package com.task.xm.presentation.viewModels

import app.cash.turbine.test
import com.task.xm.core.datasources.Failure
import com.task.xm.core.datasources.Resource
import com.task.xm.core.datasources.remote.ApiErrorException
import com.task.xm.domain.models.Question
import com.task.xm.domain.usecases.QuestionUseCase
import com.task.xm.domain.usecases.SubmitAnswerUseCase
import com.task.xm.utils.CoroutinesTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.ExperimentalTime

/**
 * @Author Abdullah Abo El~Makarem on 09/05/2024.
 */

@ExperimentalTime
@ExperimentalCoroutinesApi
class HomeViewmodelTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()
    private val questionUseCase: QuestionUseCase = mockk()
    private val submitAnswerUseCase: SubmitAnswerUseCase = mockk()
    val viewModel = HomeViewmodel(
        coroutinesTestRule.testDispatcherProvider,
        questionUseCase,
        submitAnswerUseCase
    )

    @Before
    fun setUp() {
    }

    @Test
    fun `fetchQuestions success`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        // Given

        val questions = listOf(
            Question(
                id = 1,
                questionText = "first question",
                answer = "Answer",
                submitted = false
            )
        )
        val successResult = Resource.success(questions)
        coEvery { questionUseCase.invoke() } returns successResult

        // When
        viewModel.fetchQuestions()

        // Then
        viewModel.events.test {
            cancelAndConsumeRemainingEvents()
            Assert.assertNotNull(viewModel.events.value)
            Assert.assertEquals(
                (viewModel.events.value as HomeStateFlow.Success).questions,
                HomeStateFlow.Success(successResult.requireSuccessValue()).questions
            )
        }
    }

    @Test
    fun `fetchQuestions failed`() = coroutinesTestRule.testDispatcher.runBlockingTest {
        // Given
        val failureResult =
            Resource.failure<List<Question>>(Failure(exception = ApiErrorException()))
        coEvery { questionUseCase.invoke() } returns failureResult

        // When
        viewModel.fetchQuestions()

        // Then
        viewModel.events.test {
            cancelAndConsumeRemainingEvents()
            Assert.assertNotNull(viewModel.events.value)
            Assert.assertEquals(
                (viewModel.events.value as HomeStateFlow.Error).message,
                failureResult.failureValue().exception.toString()
            )
        }
    }

    @Test
    fun submitAnswer() {
    }
}