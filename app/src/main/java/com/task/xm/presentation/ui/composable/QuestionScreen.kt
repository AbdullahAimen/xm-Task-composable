package com.task.xm.presentation.ui.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.task.xm.domain.models.Question
import kotlinx.coroutines.launch

/**
 * @Author Abdullah Abo El~Makarem on 08/05/2024.
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SurveyQuestions(
    submitted: Boolean,
    questions: List<Question>,
    onSubmitClick: (q: Question) -> Unit,

) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        questions.size
    }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {

        // horizontal pager
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            pageSpacing = 0.dp, // Replace with the actual number of pages
            userScrollEnabled = true,
            reverseLayout = false,
            contentPadding = PaddingValues(16.dp),
            beyondBoundsPageCount = 0,
            pageSize = PageSize.Fill,
            flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
            key = null,
            pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
                Orientation.Horizontal
            ),
            pageContent = { page ->
                QuestionAndAnswerCard(
                    questions[pagerState.currentPage],
                    onSubmitClick,
                    submitted
                )
            }
        )

        // previous and next buttons
        IconButton(
            onClick = { coroutineScope.launch { pagerState.scrollToPage(pagerState.currentPage - 1) } },
            modifier = Modifier.align(Alignment.CenterStart),
            enabled = pagerState.currentPage > 0
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Previous")
        }

        IconButton(
            onClick = { coroutineScope.launch { pagerState.scrollToPage(pagerState.currentPage + 1) } },
            modifier = Modifier.align(Alignment.CenterEnd),
            enabled = pagerState.currentPage < questions.size - 1
        ) {
            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Next")
        }
    }

}

@Composable
fun QuestionAndAnswerCard(
    question: Question,
    onSubmitClick: (q: Question) -> Unit,
    submitted: Boolean

) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        elevation = CardDefaults.outlinedCardElevation(),
        border = CardDefaults.outlinedCardBorder()
    ) {
        //save state of current question's answer
        val answer = rememberSaveable { mutableStateOf(question.answer.orEmpty()) }

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Question #${question.id}  \n\n ${question.questionText}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                value = answer.value,
                onValueChange = { answer.value = it },
                label = { Text(text = "Answer") },
                placeholder = { Text(text = "type your answer here") },
            )
            Spacer(modifier = Modifier.height(50.dp))
            Button(
                onClick = {
                    question.answer = answer.value
                    onSubmitClick(question)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = submitted.not() || question.submitted.not(),
                content = {
                    Text(text = if (submitted.not()) "Submit" else "Already Submitted")
                }
            )
        }
    }
}