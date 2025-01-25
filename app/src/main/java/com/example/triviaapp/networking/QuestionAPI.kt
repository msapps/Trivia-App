package com.example.triviaapp.networking

import com.example.triviaapp.model.Question
import retrofit2.http.GET

interface QuestionAPI {
    @GET("world.json")
    suspend fun getQuestions(): Question
}