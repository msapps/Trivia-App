package com.example.triviaapp.repositories

import android.util.Log
import com.example.triviaapp.data.ApiResponse
import com.example.triviaapp.model.QuestionItem
import com.example.triviaapp.networking.QuestionAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuestionsRepository @Inject constructor(
    private val questionsApi: QuestionAPI,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val listOfQuestions = ApiResponse<ArrayList<QuestionItem>, Boolean, Exception>()

    suspend fun getQuestions(): ApiResponse<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            listOfQuestions.isLoading = true
            listOfQuestions.data = withContext(defaultDispatcher) {
                questionsApi.getQuestions()
            }
            if (listOfQuestions.data.toString().isNotEmpty()) listOfQuestions.isLoading = false
        } catch (e: Exception) {
            listOfQuestions.exception = e
            listOfQuestions.isLoading = false
            Log.e("QuestionsRepository", "getQuestions: ${e.localizedMessage}")
        }
        return listOfQuestions
    }
}