package com.example.triviaapp.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.triviaapp.components.Questions

@Composable
fun QuestionsHome(modifier: Modifier, questionsViewModel: QuestionViewModel = hiltViewModel()) {
    Questions(modifier, questionsViewModel)
}