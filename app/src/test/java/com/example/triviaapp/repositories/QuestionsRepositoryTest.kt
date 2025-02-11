package com.example.triviaapp.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.triviaapp.MainCoroutineRule
import com.example.triviaapp.model.Question
import com.example.triviaapp.networking.QuestionAPI
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class QuestionsRepositoryTest {
    @Mock
    lateinit var questionApi: QuestionAPI

    @get:Rule
    var coroutineMainDispatcher = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `Test repo_getQuestions with empty response`() = runTest {
        val expectedOutput =
            Question()//ApiResponse<ArrayList<QuestionItem>, Boolean, Exception>(data = ArrayList<QuestionItem>())
        Mockito.`when`(questionApi.getQuestions()).thenReturn(expectedOutput)
        val sut = QuestionsRepository(questionApi, coroutineMainDispatcher.dispatcher)
        val actualOutput = sut.getQuestions()
        coroutineMainDispatcher.dispatcher.scheduler.advanceUntilIdle()
        assertEquals(expectedOutput, actualOutput.data)
        assertEquals(0, actualOutput.data?.size)
    }

    @After
    fun tearDown() {
    }
}