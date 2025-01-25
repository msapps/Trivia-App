package com.example.triviaapp.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triviaapp.model.QuestionItem
import com.example.triviaapp.screens.QuestionViewModel

@Composable
fun Questions(modifier: Modifier, questionsViewModel: QuestionViewModel) {
    val questions = questionsViewModel.questions.value.data?.toMutableList()
    var questionIndexState by rememberSaveable { mutableIntStateOf(0) }
    if (questionsViewModel.questions.value.isLoading == true) {
        Log.d("QuestionsComposable", "Loading Questions")
    } else {
        Log.d("QuestionsComposable", "Questions Loaded ${questions?.size}")
        val question = try {
            questions?.get(questionIndexState)
        } catch (e: Exception) {
            null
        }
        question?.let {
            QuestionDisplay(
                question = it,
                questionIndex = questionIndexState,
                totalQuestionsCount = questions?.count() ?: 1000,
                viewModel = questionsViewModel
            ) {
                questionIndexState = it + 1
            }
        }
    }
}


@Composable
private fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: Int,
    totalQuestionsCount: Int,
    viewModel: QuestionViewModel,
    onNextClicked: (Int) -> Unit
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val choicesState = rememberSaveable(question) { mutableStateOf(question.choices) }
    var selectedRBIndexState by rememberSaveable(question) { mutableStateOf<Int?>(null) }
    var isCorrectAnswerSelectedState by rememberSaveable(question) { mutableStateOf<Boolean?>(null) }
    val updateAnswer: (Int) -> Unit = remember(question) {
        { selectedIndex ->
            selectedRBIndexState = selectedIndex
            selectedRBIndexState?.let {
                isCorrectAnswerSelectedState = choicesState.value?.get(it) == question.answer
            }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.LightGray
    ) {
        Column(
            Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionHeader(
                questionNumber = questionIndex + 1,
                outOf = totalQuestionsCount
            )
            DottedDivider(pathEffect)
            Column(
                modifier = Modifier.padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = question.question.orEmpty(),
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxHeight(fraction = 0.3f)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                choicesState.value?.forEachIndexed { index, choice ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                color = Color.Magenta,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(
                                RoundedCornerShape(50f)
                            )
                            .background(color = Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedRBIndexState == index,
                            onClick = { updateAnswer(index) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if (isCorrectAnswerSelectedState == true) Color.Green
                                else Color.Red
                            )
                        )
                        Text(text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = if (isCorrectAnswerSelectedState == true && selectedRBIndexState == index) {
                                        Color.Green
                                    } else if (isCorrectAnswerSelectedState == false && selectedRBIndexState == index)
                                        Color.Red
                                    else Color.Black,
                                    fontSize = 12.sp
                                )
                            ) {
                                append(choice.orEmpty())
                            }
                        })
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Button(shape = RoundedCornerShape(45.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Yellow
                    ),
                    onClick = { onNextClicked(questionIndex) }) {
                    Text(
                        "Next >>",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

        }
    }

}

@Preview
@Composable
private fun QuestionHeader(
    questionNumber: Int = 10,
    outOf: Int = 100
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    color = Color.Black,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Question $questionNumber/")
            }
            withStyle(SpanStyle(color = Color.Black, fontSize = 12.sp)) {
                append(outOf.toString())
            }
        },
        modifier = Modifier.padding(20.dp)
    )
}

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun DottedDivider(
    pathEffect: PathEffect = PathEffect.dashPathEffect(
        floatArrayOf(10f, 10f),
        0f
    )
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = Color.Black, pathEffect = pathEffect,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f)
        )
    }
}