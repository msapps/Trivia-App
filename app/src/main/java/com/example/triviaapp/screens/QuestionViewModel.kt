package com.example.triviaapp.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.data.ApiResponse
import com.example.triviaapp.model.QuestionItem
import com.example.triviaapp.repositories.QuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class QuestionViewModel
@Inject constructor(private val repository: QuestionsRepository) : ViewModel() {

    var questions = mutableStateOf(ApiResponse<ArrayList<QuestionItem>, Boolean, Exception>())
    var questionsList: List<QuestionItem>? = null

    init {
        viewModelScope.launch {
            questionsList = fetchQuestions().data
        }
    }

    suspend fun fetchQuestions(): ApiResponse<ArrayList<QuestionItem>, Boolean, Exception> =
        suspendCoroutine {
        viewModelScope.launch {
            questions.value.isLoading = true
            questions.value = repository.getQuestions()
            it.resume(questions.value)
        }
    }
}