package com.example.triviaapp.repositories

import android.util.Log
import com.example.triviaapp.data.ApiResponse
import com.example.triviaapp.model.QuestionItem
import com.example.triviaapp.networking.QuestionAPI
import javax.inject.Inject

class QuestionsRepository @Inject constructor(
    private val questionsApi: QuestionAPI
) {
    private val listOfQuestions = ApiResponse<ArrayList<QuestionItem>, Boolean, Exception>()

    suspend fun getQuestions(): ApiResponse<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            listOfQuestions.isLoading = true
            listOfQuestions.data = questionsApi.getQuestions()
            if (listOfQuestions.data.toString().isNotEmpty()) listOfQuestions.isLoading = false
        } catch (e: Exception) {
            listOfQuestions.exception = e
            Log.e("QuestionsRepository", "getQuestions: ${e.localizedMessage}")
        }
        return listOfQuestions
    }
}