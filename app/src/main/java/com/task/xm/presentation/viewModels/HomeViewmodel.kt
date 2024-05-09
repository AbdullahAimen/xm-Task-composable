package com.task.xm.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.xm.core.coroutines.CoroutineDispatcherProvider
import com.task.xm.domain.models.Question
import com.task.xm.domain.usecases.QuestionUseCase
import com.task.xm.domain.usecases.SubmitAnswerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author Abdullah Abo El~Makarem on 08/05/2024.
 */

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val questionUseCase: QuestionUseCase,
    private val submitAnswerUseCase: SubmitAnswerUseCase
) : ViewModel() {

    private val _events = MutableStateFlow<HomeStateFlow>(HomeStateFlow.Loading)
    val events: MutableStateFlow<HomeStateFlow> = _events

    val questions = mutableListOf<Question>()

    init {
        fetchQuestions()
    }

    fun fetchQuestions() {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            emitEvent(HomeStateFlow.Loading)
            questionUseCase.invoke().onSuccess {
                questions.clear()
                questions.addAll(it.orEmpty())
                emitEvent(HomeStateFlow.Success(it.orEmpty()))
            }.onFailure {
                emitEvent(HomeStateFlow.Error(it?.exception.toString()))
            }
        }
    }

    fun submitAnswer(question: Question) {
        viewModelScope.launch(coroutineDispatcherProvider.io()) {
            emitEvent(HomeStateFlow.Loading)
            submitAnswerUseCase.invoke(question.id ?: 0, question.answer.orEmpty()).onSuccess {
                emitEvent(HomeStateFlow.AnswerSubmissionSuccess)
                questions
                    .first { it.id == question.id }
                    .apply { submitted = true }
            }.onFailure {
                emitEvent(HomeStateFlow.AnswerSubmissionError)
            }

            delay(2000)
            emitEvent(HomeStateFlow.HideNotification)
        }
    }

    private fun emitEvent(event: HomeStateFlow) {
        viewModelScope.launch(context = coroutineDispatcherProvider.io()) {
            _events.emit(event)
        }
    }
}

sealed interface HomeStateFlow {
    data object Loading : HomeStateFlow
    class Success(val questions: List<Question>) : HomeStateFlow
    class Error(val message: String) : HomeStateFlow
    data object AnswerSubmissionSuccess : HomeStateFlow
    data object AnswerSubmissionError : HomeStateFlow
    data object HideNotification : HomeStateFlow


}