package com.example.triviaapp.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.data.ApiResponse
import com.example.triviaapp.model.QuestionItem
import com.example.triviaapp.repositories.QuestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel
@Inject constructor(private val repository: QuestionsRepository) : ViewModel() {

    var questions = mutableStateOf(ApiResponse<ArrayList<QuestionItem>, Boolean, Exception>())

    init {
        fetchQuestions()
    }

    private fun fetchQuestions(): ApiResponse<ArrayList<QuestionItem>, Boolean, Exception> {
        viewModelScope.launch {
            questions.value.isLoading = true
            questions.value = withContext(Dispatchers.IO) { repository.getQuestions() }
            /*questions.value.copy(data = withContext(Dispatchers.IO) { repository.getQuestions().data })
            if (questions.value.data.toString().isNotEmpty()) questions.value.isLoading = false*/
        }
        return questions.value
    }
}