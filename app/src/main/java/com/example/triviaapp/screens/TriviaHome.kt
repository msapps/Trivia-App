package com.example.triviaapp.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.triviaapp.components.Questions
import kotlinx.coroutines.CoroutineScope

@Composable
fun QuestionsHome(
    modifier: Modifier, questionsViewModel: QuestionViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()
    Questions(modifier, questionsViewModel) {
        ShowSnackBar(it, snackbarHostState, scope)

    }
}

@Composable
fun ShowSnackBar(msg: String, snackbarHostState: SnackbarHostState, scope: CoroutineScope) {
    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(msg)
    }
}