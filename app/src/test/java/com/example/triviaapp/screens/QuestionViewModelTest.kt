package com.example.triviaapp.screens

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.triviaapp.MainCoroutineRule
import com.example.triviaapp.data.ApiResponse
import com.example.triviaapp.model.QuestionItem
import com.example.triviaapp.repositories.QuestionsRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

class QuestionViewModelTest {
    private var questionsRepository = mock(QuestionsRepository::class.java)

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
    }

    @Test
    fun emptyQuestionsResponse() = runTest {
        Mockito.`when`(questionsRepository.getQuestions())
            .thenReturn(ApiResponse(data = ArrayList<QuestionItem>()))
        val sut = QuestionViewModel(questionsRepository)
        sut.fetchQuestions()
        mainCoroutineRule.dispatcher.scheduler.advanceUntilIdle()
        assertEquals(emptyList<QuestionItem>(), sut.questionsList)
    }

    @Test
    fun `fetch questions error response`() = runTest {
        val expectedResult =
            ApiResponse<ArrayList<QuestionItem>, Boolean, Exception>(exception = Exception("Something went wrong"))
        Mockito.`when`(questionsRepository.getQuestions()).thenReturn(expectedResult)
        val sut = QuestionViewModel(questionsRepository)
        val actualResult = sut.fetchQuestions()
        mainCoroutineRule.dispatcher.scheduler.advanceUntilIdle()
        assertEquals(expectedResult, actualResult)
    }

    @After
    fun tearDown() {
    }
}