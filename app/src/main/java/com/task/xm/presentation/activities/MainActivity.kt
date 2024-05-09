package com.task.xm.presentation.activities

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.task.xm.presentation.ui.composable.SurveyQuestions
import com.task.xm.presentation.ui.composable.TopAppBar
import com.task.xm.presentation.viewModels.HomeStateFlow
import com.task.xm.presentation.viewModels.HomeViewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val homeViewmodel: HomeViewmodel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App(homeViewmodel = homeViewmodel)
        }
    }
}

@Composable
fun App(
    navController: NavHostController = rememberNavController(),
    homeViewmodel: HomeViewmodel
) {

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.ScreenA.name
    )

    Scaffold(topBar = {
        TopAppBar(currentScreen = currentScreen,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() })
    }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            AppNavHost(navController, homeViewmodel)
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    homeViewmodel: HomeViewmodel,
) {
    val (isVisible, setIsVisible) = remember { mutableStateOf(false) }
    val (isSuccess, setIsSuccess) = remember { mutableStateOf(false) }
    val (loading, setIsLoading) = remember { mutableStateOf(false) }
    val submitted = rememberSaveable { mutableStateOf(false) }
    NotificationBanner(isVisible, isSuccess)
    NavHost(
        navController = navController,
        startDestination = AppScreen.ScreenA.name,
    ) {
        composable(route = AppScreen.ScreenA.name) {
            StartSurvey(onNextClick = {
                navController.navigate(AppScreen.ScreenB.name)
            })
        }
        composable(route = AppScreen.ScreenB.name) {
            SurveyQuestions(submitted.value, questions = homeViewmodel.questions) {
                homeViewmodel.submitAnswer(it)
            }
        }
    }
    val uiState by homeViewmodel.events.collectAsState()
    when (uiState) {
        is HomeStateFlow.Error -> {
            Text(
                text = (uiState as HomeStateFlow.Error).message,
                modifier = Modifier
                    .padding(10.dp)
                    .border(
                        width = 2.dp,
                        color = colorResource(id = R.color.holo_red_light),
                    )
            )
            setIsLoading(false)
        }

        HomeStateFlow.Loading -> {
            setIsLoading(true)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                if (loading)
                    CircularProgressIndicator()
            }
        }

        is HomeStateFlow.Success -> {
            setIsLoading(false)
            val data = (uiState as HomeStateFlow.Success).questions
            SurveyQuestions(submitted.value, questions = homeViewmodel.questions) {
                homeViewmodel.submitAnswer(it)
            }
        }

        HomeStateFlow.AnswerSubmissionError -> {
            setIsSuccess(false)
            setIsVisible(true)
            submitted.value = false
        }

        HomeStateFlow.AnswerSubmissionSuccess -> {
            setIsSuccess(true)
            setIsVisible(true)
            submitted.value = true
        }

        HomeStateFlow.HideNotification -> {
            setIsVisible(false)
        }
    }

}

@Composable
fun NotificationBanner(isVisible: Boolean, isSuccess: Boolean) {
    if (isVisible) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    if (isSuccess) colorResource(id = R.color.holo_green_light) else colorResource(
                        id = R.color.holo_red_light
                    )
                ),
            contentAlignment = Alignment.CenterStart

        ) {
            Text(
                text = if (isSuccess) "Success" else " Failure!",
                fontSize = 21.sp,
                color = colorResource(id = R.color.black),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterStart)

            )
        }
    }
}


@Composable
private fun StartSurvey(
    onNextClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(
            onClick = onNextClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Start Survey")
        }
    }
}

enum class AppScreen(val title: String) {
    ScreenA(title = "Start Survey"), // start screen
    ScreenB(title = "Questions"),
}